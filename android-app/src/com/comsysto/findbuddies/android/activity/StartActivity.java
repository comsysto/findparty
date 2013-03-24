package com.comsysto.findbuddies.android.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.StrictMode;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;


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

        Intent intent = null;

        if(isLoggedIn()) {
            intent = new Intent(this, MyPartiesActivity.class);

        } else {
            intent = new Intent(this, LoginActivity.class);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
	}

    private boolean isLoggedIn() {
        return ((PartyManagerApplication)getApplication()).getAccountService().hasAccount();
    }

}
