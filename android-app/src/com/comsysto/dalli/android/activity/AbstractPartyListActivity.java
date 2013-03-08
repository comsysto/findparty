package com.comsysto.dalli.android.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.adapter.PartyListAdapter;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.dalli.android.menu.OptionMenuHandler;
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

	private ArrayAdapter<com.comsysto.findparty.Party> arrayAdapter;
	protected List<Party> parties;
	private OptionMenuHandler optionMenuHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.optionMenuHandler = new OptionMenuHandler(this);
		registerForContextMenu(getListView());
	}

	@Override
	protected void onResume() {
		super.onResume();
        String username = getUsername();
        if(username != null) {
            initArrayAdapter(username);
        } else {
            finish();
        }
	}

    private String getUsername() {
        return getPartyManagerApplication().getAccountService().getUsername();
    }

    protected void initArrayAdapter(String username) {
		this.parties = getPartyManagerApplication().getPartyService().getAllParties(username);
        Log.i("MY_PARTIES", "retrieved my parties from server: " + this.parties);
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
            Log.i("MY_PARTIES", "deleting party: " +selectedParty);
			getPartyManagerApplication().getPartyService().delete(selectedParty.getId());
            this.notifyDataSetChanged();
			return true;
		case R.id.edit_party:
            Log.i("MY_PARTIES", "editing party: " +selectedParty);
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

}
