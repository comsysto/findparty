package com.comsysto.findbuddies.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import com.comsysto.dalli.android.R;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;

/**
 * Shows and handles the Preferences of the App
 * 
 * @author stefandjurasic
 *
 */
public class Preferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}


	
	@Override
	/**
	 * when the settings are left, we go back to main screen
	 */
	public void onBackPressed() {
		((PartyManagerApplication)getApplication()).initializePartyService();
		Intent intent = new Intent(this, MyPartiesActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	    startActivity(intent);
	}
	
}
