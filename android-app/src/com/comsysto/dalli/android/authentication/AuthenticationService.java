package com.comsysto.dalli.android.authentication;

import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * The {@link AccountManager} will use this service to obtain the {@link AccountAuthenticator} of the app.
 * 
 * @author stefandjurasic
 *
 */
public class AuthenticationService extends Service {

    private static final String TAG = "AuthenticationService";
    private AccountAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
        }
        Log.d(TAG, "comSysto Authentication Service started.");
        mAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    public void onDestroy() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "comSysto Authentication Service stopped.");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG,
                "getBinder()...  returning the AccountAuthenticator binder for intent "
                    + intent);
        }
        return mAuthenticator.getIBinder();
    }

}
