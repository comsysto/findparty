package com.comsysto.findbuddies.android.activity;

import android.accounts.AccountAuthenticatorActivity;
import android.app.ActionBar;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disableTitleInActionBar();
        setContentView(R.layout.login);

        //if there is an Account on device -> we use this and proceed to Dashboard.
        //else -> we create the view and display it.
        if(isLoggedIn()) {
            startApp();
        } else {
            createLoginView();
        }
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

    private boolean isLoggedIn() {
        return getPartyManangerApplication().getAccountService().hasAccount();
    }



    protected void register(String username, String password) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivity(intent);
        finish();
    }

    protected void login(String username, String password) {
        if(isEmpty(username) || isEmpty(password)) {
            Log.d(TAG, "login failed: username and/or password may not be empty");
            error.setText(getString(R.string.LOGIN_FAILED_LABEL));
            error.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            error.setVisibility(View.VISIBLE);
        }
        else if (!authenticate(username, password)) {
            Log.d(TAG, "login failed: username/password not authenticated (" +username + "/" + password+")");
            error.setText(getString(R.string.LOGIN_FAILED_LABEL));
            error.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            error.setVisibility(View.VISIBLE);
        } else {
            startApp();
        }
    }

    private boolean authenticate(String username, String password) {
        return getPartyManangerApplication().authenticate(username, password);
    }

    private void startApp() {
        Intent intent = new Intent(this, MyPartiesActivity.class);
        startActivity(intent);
        finish();
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

    private PartyManagerApplication getPartyManangerApplication() {
        return ((PartyManagerApplication)getApplication());
    }

    @Override
    public void onBackPressed(){
        if (!(this instanceof RegisterActivity)) {
            moveTaskToBack(true);
        } else {
            super.onBackPressed();
        }
    }
}
