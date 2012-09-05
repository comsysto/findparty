package com.comsysto.dalli.android.activity;

import android.view.View;
import android.view.View.OnClickListener;
import com.comsysto.findparty.Party;

/**
 * Activity for creating Tasks
 * 
 * @author stefandjurasic
 *
 */
public class CreatePartyActivity extends PartyActivity {

	@Override
	public OnClickListener getOnClickListener() {
		return new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Party newParty= new Party();
                newParty.setCategory(getCategory());
				getTaskManagerApplication().createParty(newParty);
                goToSplashScreen(CreatePartyActivity.this);
            }
		};
	}
	
	public String getCategory() {
		return (String) getIntent().getExtras().get("category");
	}
}
