package com.comsysto.findbuddies.android.activity;

import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.adapter.PartyListAdapter;
import com.comsysto.findbuddies.android.application.Constants;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.menu.OptionMenuHandler;
import com.comsysto.findbuddies.android.service.async.party.UpdatePartyMode;
import com.comsysto.findbuddies.android.service.async.party.UpdatePartyAsync;
import com.comsysto.findbuddies.android.service.async.party.UpdatePartyCallback;
import com.comsysto.findbuddies.android.widget.LoadingProgressDialog;
import com.comsysto.findparty.Party;

import java.util.List;

/**
 * Acitivty for displaying Users Activities!
 * 
 * 
 * @author stefandjurasic
 *
 */
public class MyPartiesActivity extends ListActivity implements UpdatePartyCallback {

    private ActionMode mActionMode;

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }


    private static final String LOG_MY_PARTIES = Constants.LOG_APP_PREFIX+ "_MY_PARTIES";

    private ArrayAdapter<Party> arrayAdapter;
    protected List<Party> parties;
    private OptionMenuHandler optionMenuHandler;
    private LoadingProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.my_parties);
        disableTitleInActionBar();
        this.optionMenuHandler = new OptionMenuHandler(this);
        getListView().setChoiceMode(ListView.CHOICE_MODE_NONE);
        getListView().setSelector(android.R.color.holo_blue_dark);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getPartyManagerApplication().getAccountService().hasAccount()) {
            dialog = new LoadingProgressDialog(this, "Loading. Please wait...", true);
            initArrayAdapter();
        }
        else {
            redirectToLoginPage();
        }
    }

    private void redirectToLoginPage() {
        Intent intent = new Intent(this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private String getUsername() {
        return getPartyManagerApplication().getAccountService().getUsername();
    }

    protected void initArrayAdapter() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                String userName = getUsername();
                MyPartiesActivity.this.parties = getPartyManagerApplication().getPartyService().getAllParties(userName);
                Log.i(LOG_MY_PARTIES, "retrieved my parties from server: " + MyPartiesActivity.this.parties);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setArrayAdapter(new PartyListAdapter(MyPartiesActivity.this, MyPartiesActivity.this.parties));
                setListAdapter(getArrayAdapter());
                dialog.hide();
            }
        }.execute();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        v.setSelected(true);

        if (mActionMode == null) {
            mActionMode = this.startActionMode(mActionModeCallback);
        }
        mActionMode.setTag(getArrayAdapter().getItem((int) id));
    }



    public void setArrayAdapter(ArrayAdapter<Party> arrayAdapter) {
        this.arrayAdapter = arrayAdapter;
    }

    public ArrayAdapter<Party> getArrayAdapter() {
        return arrayAdapter;
    }

    public PartyManagerApplication getPartyManagerApplication() {
        return (PartyManagerApplication) getApplication();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return this.optionMenuHandler.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.optionMenuHandler.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    private void disableTitleInActionBar() {
        ActionBar ab = getActionBar();
        ab.setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.dismiss();
    }


    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.owner_party_menu, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            final Party selectedParty = (Party)mode.getTag();
            switch (item.getItemId()) {
                case R.id.delete_party:
                    Log.i(LOG_MY_PARTIES, "deleting party: " +selectedParty);
                    MyPartiesActivity.this.dialog.show();
                    new UpdatePartyAsync(MyPartiesActivity.this).execute(selectedParty);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                case R.id.edit_party:
                    Log.i(LOG_MY_PARTIES, "editing party: " +selectedParty);
                    getPartyManagerApplication().setSelectedParty(selectedParty);
                    Intent intent = new Intent(MyPartiesActivity.this,
                            EditPartyActivity.class);
                    startActivity(intent);
                    mode.finish(); // Action picked, so close the CAB
                    return true;
                default:
                    return false;

            }
      }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            getListView().clearChoices();
            getListView().requestLayout();
            mActionMode = null;
        }
    };

    @Override
    public void successOnPartyUpdate(String partyId) {
        this.dialog.hide();
        Party deletedParty = getPartyWithId(this.parties, partyId);
        parties.remove(deletedParty);
        getArrayAdapter().notifyDataSetChanged();
    }

    private Party getPartyWithId(List<Party> parties, String partyId) {
        if (partyId == null) {
            throw new IllegalArgumentException("partyId should never be null");
        }
        for (Party party : parties) {
            if (partyId.equals(party.getId())) {
                return party;
            }
        }
        return null;
    }

    @Override
    public void errorOnPartyUpdate() {
        this.dialog.hide();
        Toast.makeText(this, getString(R.string.party_delete_error), Toast.LENGTH_LONG);
    }

    @Override
    public UpdatePartyMode getUpdatePartyAsyncMode() {
        return UpdatePartyMode.DELETE;
    }
}
