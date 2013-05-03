package com.comsysto.findbuddies.android.activity;

import android.accounts.AccountAuthenticatorActivity;
import android.app.ActionBar;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.comsysto.dalli.android.R;
import com.comsysto.findbuddies.android.application.Constants;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findbuddies.android.widget.LoadingProgressDialog;
import org.springframework.web.client.RestClientException;

/**
 * Displays the login page.
 *
 * @author stefandjurasic
 */
public class LoginActivity extends AccountAuthenticatorActivity {

    private final static String TAG = Constants.LOG_AUTH_PREFIX + LoginActivity.class.getSimpleName();

    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    protected EditText userName;
    protected EditText password;
    protected TextView error;

    protected Button loginButton;
    protected Button registerButton;
    LoadingProgressDialog loadingProgressDialog;

    @Override
    protected void onResume() {
        super.onResume();
        this.loadingProgressDialog = new LoadingProgressDialog(this, getLoadingProgressDialogText(), false );
    }

    protected String getLoadingProgressDialogText() {
        return "Verifiying credentials...";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableTitleInActionBar();
        setContentView(R.layout.login);

        createLoginView();
    }

    private void disableTitleInActionBar() {
        ActionBar ab = getActionBar();
        ab.setDisplayShowTitleEnabled(false);
    }

    private void createLoginView() {
        loginButton = (Button) findViewById(R.id.LOGIN_BUTTON);
        registerButton = (Button) findViewById(R.id.REGISTER_BUTTON);
        error = (TextView) findViewById(R.id.error);

        userName = (EditText) findViewById(R.id.editText1);
        password = (EditText) findViewById(R.id.editText2);

        OnTouchListener fadeOutOnTouchListener = createErrorTextViewListener();
        userName.setOnTouchListener(fadeOutOnTouchListener);
        password.setOnTouchListener(fadeOutOnTouchListener);

        createLoginButtonListener();
        createRegisterButtonListener();
    }



    protected void register(String username, String password) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivity(intent);
    }

    protected void login(String username, String password) {
        if(isEmpty(username) || isEmpty(password)) {
            Log.d(TAG, "login failed: username and/or password may not be empty");
            error.setText(getErrorText());
            error.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            error.setVisibility(View.VISIBLE);
        } else {
            loadingProgressDialog.show();
            authenticate(username, password);
        }
    }

    void handleResponse(boolean success) {
        loadingProgressDialog.hide();
        if (!success) {
            error.setText(getErrorText());
            error.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            error.setVisibility(View.VISIBLE);
        } else {
                getPartyManagerApplication().goToStart(this);
        }

    }

    String getErrorText() {
        return getString(R.string.LOGIN_FAILED_LABEL);
    }

    private void authenticate(final String username, final String password) {
        new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... params) {
                try {
                    return LoginActivity.this.getPartyManagerApplication().authenticate(username, password);

                } catch (RestClientException e) {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean authenticated) {
                handleResponse(authenticated);
            }
        }.execute();
    }


    protected void setLoginButtonVisible(boolean visible) {
        if (visible) {
            loginButton.setVisibility(View.VISIBLE);
        } else {
            loginButton.setVisibility(View.GONE);
        }
    }

    //--- Listener ---//
    private void createRegisterButtonListener() {
        if (registerButton != null) {
            registerButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    register(userName.getText().toString(), password.getText().toString());
                }
            });
        }
    }

    private void createLoginButtonListener() {
        if (loginButton != null) {
            loginButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    login(userName.getText().toString(), password.getText().toString());
                }
            });
        }
    }

    private OnTouchListener createErrorTextViewListener() {
        return new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                error.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, android.R.anim.fade_out));
                error.setVisibility(View.INVISIBLE);
                return false;
            }
        };
    }

    //--- Helper functions --//
    protected boolean isEmpty(String value) {
        return value == null || value.length()==0;
    }

    @Override
    public void onBackPressed(){
        if (!(this instanceof RegisterActivity)) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }


    PartyManagerApplication getPartyManagerApplication() {
        return ((PartyManagerApplication) getApplication());
    }

}
