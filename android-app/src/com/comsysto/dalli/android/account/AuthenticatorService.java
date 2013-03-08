package com.comsysto.dalli.android.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import com.comsysto.dalli.android.application.Constants;

/**
 * User: rpelger
 * Date: 08.03.13
 */
public class AuthenticatorService extends Service {

    private static final String TAG = Constants.LOG_PREFIX + AuthenticatorService.class.getSimpleName();
    private AccountAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        Log.d(TAG, "comSysto findbuddies-app Authentication Service started.");
        mAuthenticator = new AccountAuthenticator(this);
    }

    @Override
    public void onDestroy() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "comSysto findbuddies-app Authentication Service stopped.");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mAuthenticator.getIBinder();
    }
}
