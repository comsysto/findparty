package com.comsysto.dalli.android.authentication;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.comsysto.dalli.android.application.Constants;

/**
 * The {@link AccountManager} will use this service to obtain the {@link AccountAuthenticator} of the app.
 * 
 * @author stefandjurasic
 *
 */
public class AuthenticationService extends Service {

    private static final String TAG = Constants.LOG_AUTH_PREFIX + AuthenticationService.class.getSimpleName();
    private AccountAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        Log.d(TAG, "comSysto Authentication Service started.");
        mAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "comSysto Authentication Service stopped.");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "getBinder()...  returning the AccountAuthenticator binder for intent " + intent);
        return mAuthenticator.getIBinder();
    }

}
