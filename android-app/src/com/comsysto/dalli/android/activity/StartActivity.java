package com.comsysto.dalli.android.activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.dalli.android.authentication.AccountAuthenticator;
import com.comsysto.findparty.User;


/**
 * This Activity is the first entry point of the app.
 * 
 * Currently it looks in the AccountManagment if an account already exsits. If not the
 * AccountAuthenticator will be requested to create one which triggers the LoginPage etc.
 * 
 * If everything is fine, the {@link SplashScreenActivity} will be displayed and this Activity
 * will be cleaned up, to allow SplashScreenActivity exiting app with return button.
 * 
 * @author stefandjurasic
 *
 */
public class StartActivity extends Activity {

    @Override
	protected void onResume() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().detectAll().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        super.onResume();

	    AccountManager mAccountManager = AccountManager.get(this);
	    Account[] accountsByType = mAccountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
	    if (accountsByType.length == 0) {
	    	mAccountManager.addAccount(AccountAuthenticator.AUTH_TYPE, null, null, null, this, null, null);
	    } else {
	    	PartyManagerApplication application = (PartyManagerApplication)getApplication();
	    	
	    	User user = new User();
	    	user.setUsername(accountsByType[0].name);
	    	
	    	application.setUser(user);
	    	Intent intent = new Intent(this, SplashScreenActivity.class);
	    	startActivity(intent);
	    }
	}	
	
}
