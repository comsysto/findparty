package com.comsysto.dalli.android.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.dalli.android.authentication.AccountAuthenticator;

/**
 * Abstract activity for convenient methods.
 * 
 * @author stefandjurasic
 *
 */
public abstract class AbstractActivity extends Activity {

	/**
	 * goes to another activity
	 * 
	 * @param Activity coming from
	 * @param Class going to, careful must be an Activity!
	 */
	void goTo(Activity from, Class<?> to) {
		Intent intent = new Intent(from, to);
        //intent.putExtra("activity", "createActivity");
		startActivity(intent);
	}

    void goToSplashScreen(Activity from) {
        Intent intent = new Intent(from, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    /**
	 * returns the {@link com.comsysto.dalli.android.application.PartyManagerApplication}
	 * @return instance of {@link com.comsysto.dalli.android.application.PartyManagerApplication}
	 */
	PartyManagerApplication getPartyManagerApplication() {
		return (PartyManagerApplication) getApplication();
	}

	@Override
	public void onResume() {
		super.onResume();
		AccountManager mAccountManager = AccountManager.get(this);
		Account[] accountsByType = mAccountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
		if (accountsByType.length == 0) {
			if (isTaskRoot()) {
				Intent intent = new Intent(this, StartActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
			finish();
		}
	}
	
	
}
