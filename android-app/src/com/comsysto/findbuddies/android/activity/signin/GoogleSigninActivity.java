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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.plus.PlusClient;

public class GoogleSigninActivity extends Activity implements View.OnClickListener, ConnectionCallbacks, OnConnectionFailedListener
{
    private static final int OUR_REQUEST_CODE = 666;
    private static final String TAG = Constants.LOG_PREFIX + "Google+";

    private ProgressDialog connectionProgressDialog;

    private PlusClient plusClient;
    private ConnectionResult lastResult;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);

        plusClient = new PlusClient.Builder(this.getApplicationContext(), this, this).build();

        Log.i(TAG, "PlusClient and View created");

        connectionProgressDialog = new ProgressDialog(this);
        connectionProgressDialog.setMessage("Connecting with Google+...");

    }

    @Override
    public void onStart() {
        super.onStart();
        plusClient.connect();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.sign_in_button) {
            handleGoogleSigninClick((SignInButton) v);
        }
    }


    private void handleGoogleSigninClick(SignInButton v) {
        if(plusClient.isConnected()) {
            Toast.makeText(this, "User already connected with Google+ Signin: " + plusClient.getAccountName(), Toast.LENGTH_LONG).show();
        } else {
            if(lastResult!=null) {
                if(lastResult.hasResolution()) {
                    try {
                        lastResult.startResolutionForResult(this, OUR_REQUEST_CODE);
                    } catch (IntentSender.SendIntentException e) {
                        lastResult = null;
                        plusClient.connect();
                        Toast.makeText(this, "Fehler beim Verbindend zu Google+. Bitte erneut versuchen", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                plusClient.connect();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(!connectionProgressDialog.isShowing()) {
            connectionProgressDialog.show();
        }

        Log.i(TAG, "activityResult called with reqCode: " + requestCode + " resCode: " + resultCode);
        if(requestCode == OUR_REQUEST_CODE) {
            Log.i(TAG, "received callback with our code: " + requestCode);
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


        if(connectionProgressDialog.isShowing()) {
            connectionProgressDialog.dismiss();
        }
        String accountName = plusClient.getAccountName();
        Log.i(TAG, "PlusClient connected: " + accountName);
        Toast.makeText(this, "User connected with Google+ Signin: " + plusClient.getAccountName(), Toast.LENGTH_SHORT).show();

        Intent createAccount = new Intent(this, CreateAccountActivity.class);

        createAccount.putExtra("ACCOUNT_NAME", accountName);
        startActivity(createAccount);
    }

    @Override
    public void onDisconnected() {
        Log.i(TAG, "PlusClient disconnected");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if(connectionProgressDialog.isShowing()) {
            connectionProgressDialog.dismiss();
        }

        Log.i(TAG, "onConnectionFailed listener triggered");
        lastResult = connectionResult;
    }

    @Override
    protected void onStop() {
        Log.i(TAG, "Stopping activitiy");
        super.onStop();

    }
}
