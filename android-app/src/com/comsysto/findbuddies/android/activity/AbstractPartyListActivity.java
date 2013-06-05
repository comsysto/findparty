package com.comsysto.findbuddies.android.activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.adapter.PartyListAdapter;
import com.comsysto.findbuddies.android.application.Constants;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.menu.OptionMenuHandler;
import com.comsysto.findbuddies.android.widget.LoadingProgressDialog;
import com.comsysto.findparty.Party;

import java.util.List;

/**
 * Abstract class for displaying a list of Parties. Subclasses decide which
 * List of Tasks shall be displayed. 
 * 
 * @author stefandjurasic
 *
 */
public abstract class AbstractPartyListActivity extends ListActivity {

    private static final String LOG_MY_PARTIES = Constants.LOG_APP_PREFIX+ "_MY_PARTIES";

	private ArrayAdapter<com.comsysto.findparty.Party> arrayAdapter;
	protected List<Party> parties;
	private OptionMenuHandler optionMenuHandler;
    private LoadingProgressDialog dialog;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        disableTitleInActionBar();
		this.optionMenuHandler = new OptionMenuHandler(this);
		registerForContextMenu(getListView());
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
                AbstractPartyListActivity.this.parties = getPartyManagerApplication().getPartyService().getAllParties(userName);
                Log.i(LOG_MY_PARTIES, "retrieved my parties from server: " + AbstractPartyListActivity.this.parties);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                setArrayAdapter(new PartyListAdapter(AbstractPartyListActivity.this, AbstractPartyListActivity.this.parties));
                setListAdapter(getArrayAdapter());
                dialog.hide();
            }
        }.execute();
	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        v.showContextMenu();
    }

    @Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.owner_party_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		final Party selectedParty = getArrayAdapter().getItem((int)info.id);

		switch (item.getItemId()) {
		case R.id.delete_party:
            Log.i(LOG_MY_PARTIES, "deleting party: " +selectedParty);
            AbstractPartyListActivity.this.dialog.show();
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    getPartyManagerApplication().getPartyService().delete(selectedParty.getId());
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    AbstractPartyListActivity.this.dialog.hide();
                    parties.remove(selectedParty);
                    getArrayAdapter().notifyDataSetChanged();
                }
            }.execute();
			return true;
		case R.id.edit_party:
            Log.i(LOG_MY_PARTIES, "editing party: " +selectedParty);
			getPartyManagerApplication().setSelectedParty(selectedParty);
			Intent intent = new Intent(this,
					EditPartyActivity.class);
			startActivity(intent);
			return true;
		default:
			return super.onContextItemSelected(item);

		}
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
    protected Dialog onCreateDialog(int id) {
        Log.i(LOG_MY_PARTIES, "onCreatDialog called");

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Splash Screeen Button clicked");
        alertDialog
                .setMessage("inital text - needed or you can't change it later in onprepare.... bug???");
        return alertDialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        Log.i(LOG_MY_PARTIES, "onPrepareDialog called");

        switch (id) {
            case 5:
                ((AlertDialog) dialog).setTitle("Service not ready");
                ((AlertDialog) dialog).setMessage("Please wait until backend is initialized");
                break;
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.dismiss();
    }
}
