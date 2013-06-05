package com.comsysto.findbuddies.android.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findparty.Party;

/**
 * Activity for editing exisiting Tasks
 * 
 * @author stefandjurasic
 *
 */
public class EditPartyActivity extends PartyActivity {


	/**
	 * Initializes fields from existing Task
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.create_party).setVisibility(View.INVISIBLE);
        this.saveButton.setText(R.string.SAVE_PARTY_BUTTON);
    }

	
	public String getCategory() {
        return getPartyManagerApplication().getSelectedParty().getCategory();
	}

    @Override
    void submit() {
        getPartyManagerApplication().getPartyService().update(party);
    }

    @Override
    protected Party getParty() {
        return getPartyManagerApplication().getSelectedParty();
    }
}
