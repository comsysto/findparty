package com.comsysto.dalli.android.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.comsysto.dalli.android.R;
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

        this.saveButton.setText(R.string.SAVE_PARTY_BUTTON);
    }


	@Override
	public OnClickListener getOnClickListener() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				getPartyManagerApplication().getPartyService().update(party);
				goToSplashScreen(EditPartyActivity.this);
			}
		};
	}		
	
	public String getCategory() {
        return getPartyManagerApplication().getSelectedParty().getCategory();
	}

    @Override
    protected Party getParty() {
        return getPartyManagerApplication().getSelectedParty();
    }
}
