package com.comsysto.findbuddies.android.activity.signin;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.comsysto.findbuddies.android.application.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.*;
import com.google.android.gms.plus.PlusClient;

/**
 * User: PELGERO
 * Date: 15.05.13
 */
public class GoogleSigninActivity extends Activity implements ConnectionCallbacks, OnConnectionFailedListener {

    private final static String TAG = Constants.LOG_PREFIX + GoogleSigninActivity.class.getName();

    private PlusClient plusClient;
    private ConnectionResult lastResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        plusClient = new PlusClient.Builder(this.getApplicationContext(), this, this).build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "Connecting Google+ client...");
        plusClient.connect();
    }

    @Override
    public void onConnected() {
        Log.i(TAG, "Google+ client connected!");
    }

    @Override
    public void onDisconnected() {
        Log.i(TAG, "Google+ client disconnected!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Failure connecting Google+ client [ErrorCode: " + connectionResult.getErrorCode()+"]");
        lastResult = connectionResult;
    }
}
