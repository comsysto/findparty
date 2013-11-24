package com.comsysto.findbuddies.android.activity;

import android.os.Bundle;
import android.view.View;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.service.async.party.UpdatePartyMode;
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
    }

	
	public String getCategory() {
        return getPartyManagerApplication().getSelectedParty().getCategory();
	}

    @Override
    protected Party getParty() {
        return getPartyManagerApplication().getSelectedParty();
    }

    @Override
    public UpdatePartyMode getUpdatePartyAsyncMode() {
        return UpdatePartyMode.UPDATE;
    }
}
