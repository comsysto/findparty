package com.comsysto.findbuddies.android.activity.signin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.activity.StartActivity;
import com.comsysto.findbuddies.android.application.Constants;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

public class GoogleSigninActivity extends Activity implements View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener
{
    private static final int OUR_REQUEST_CODE = 666;
    private static final String TAG = Constants.LOG_PREFIX + "Google+";

    private PlusClient plusClient;
    private ConnectionResult lastResult;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);

        plusClient = new PlusClient.Builder(this.getApplicationContext(), this, this).build();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(PartyManagerApplication.getInstance().isConnected()) {
            plusClient.connect();
        } else {
            Toast.makeText(this, getString(R.string.NO_INTERNET_CONNECTION), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sign_in_button) {
            handleGoogleSigninClick((SignInButton) v);
        }
    }


    private void handleGoogleSigninClick(SignInButton v) {
        if(plusClient.isConnected()) {
            Toast.makeText(this, getString(R.string.USER_ALREADY_CONNECTED) + " " + plusClient.getAccountName(), Toast.LENGTH_LONG).show();
        } else {
            if(lastResult!=null) {
                if(lastResult.hasResolution()) {
                    try {
                        lastResult.startResolutionForResult(this, OUR_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        lastResult = null;
                        plusClient.connect();
                        Toast.makeText(this, getString(R.string.ERROR_CONNECTING_TO_GOOGLE), Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                plusClient.connect();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == OUR_REQUEST_CODE) {
            if(!plusClient.isConnected()) {
                plusClient.connect();
            }
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        String action = getIntent().getAction();
        if(action!=null && action.equals("GOOGLE_PLUS_LOGOUT")) {
            plusClient.clearDefaultAccount();
            plusClient.disconnect();
            Intent restartApp = new Intent(this, StartActivity.class);
            startActivity(restartApp);
            return;
        }

        String accountName = plusClient.getAccountName();
        Toast.makeText(this, getString(R.string.USER_CONNECTED_WITH_GOOGLE) + " " + plusClient.getAccountName(), Toast.LENGTH_SHORT).show();

        Person.Image image = plusClient.getCurrentPerson().getImage();

        PartyManagerApplication.getInstance().getAccountService().createAccount(accountName, image.getUrl());
        startActivity(new Intent(this, StartActivity.class));
    }

    @Override
    public void onDisconnected() {
        Log.i(TAG, "Google PlusClient disconnected!");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        lastResult = connectionResult;
    }

}
