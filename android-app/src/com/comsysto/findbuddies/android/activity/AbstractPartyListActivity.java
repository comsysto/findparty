package com.comsysto.findbuddies.android.activity;

import android.app.*;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.comsysto.dalli.android.R;
import com.comsysto.findbuddies.android.adapter.PartyListAdapter;
import com.comsysto.findbuddies.android.application.Constants;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.menu.OptionMenuHandler;
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
    private ProgressDialog dialog;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        disableTitleInActionBar();
		this.optionMenuHandler = new OptionMenuHandler(this);
		registerForContextMenu(getListView());

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading. Please wait...");
	}

	@Override
	protected void onResume() {
		super.onResume();
        showProgressDialogIfNotReady();
	}

    private String getUsername() {
        return getPartyManagerApplication().getAccountService().getUsername();
    }

    protected void initArrayAdapter(String username) {
		this.parties = getPartyManagerApplication().getPartyService().getAllParties(username);
        Log.i(LOG_MY_PARTIES, "retrieved my parties from server: " + this.parties);
		setArrayAdapter(new PartyListAdapter(this, parties));
		setListAdapter(getArrayAdapter());
//        getArrayAdapter().notifyDataSetChanged();
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
		Party selectedParty = getArrayAdapter().getItem((int)info.id);

		switch (item.getItemId()) {
		case R.id.delete_party:
            Log.i(LOG_MY_PARTIES, "deleting party: " +selectedParty);
			getPartyManagerApplication().getPartyService().delete(selectedParty.getId());
            this.notifyDataSetChanged();
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

	public void notifyDataSetChanged() {
		initArrayAdapter(getUsername());
	}

    private void disableTitleInActionBar() {
        ActionBar ab = getActionBar();
        ab.setDisplayShowTitleEnabled(false);
    }


    private void showProgressDialogIfNotReady() {
        if (!dialog.isShowing() && !getPartyManagerApplication().isReady()) {
            dialog.show();
            new AsyncTask<Void, Void, Void>() {

                @Override
                protected Void doInBackground(Void... params) {
                    while (!getPartyManagerApplication().isReady()) {
                        try{
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        }
                    }
                    return null;
                }

                protected void onPostExecute(Void result) {
                    dialog.hide();
                    String username = getUsername();
                    if(username != null) {
                        initArrayAdapter(username);
                    } else {
                        finish();
                    }
                };

            }.execute();
        }
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
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();    //To change body of overridden methods use File | Settings | File Templates.
    }


}
