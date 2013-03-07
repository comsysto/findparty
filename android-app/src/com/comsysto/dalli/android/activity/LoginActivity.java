package com.comsysto.dalli.android.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
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
import com.comsysto.dalli.android.application.Constants;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.dalli.android.authentication.AccountAuthenticator;
import com.comsysto.dalli.android.model.UserAccount;
import com.comsysto.findparty.User;
import org.w3c.dom.UserDataHandler;

/**
 * Displays the login page.
 *
 * @author stefandjurasic
 */
public class LoginActivity extends AccountAuthenticatorActivity {

    private final static String TAG = Constants.LOG_AUTH_PREFIX + LoginActivity.class.getSimpleName();

    public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
    boolean loggedIn = false;
    protected EditText userName;
    protected EditText password;
    protected AccountManager accountManager;
    protected TextView error;

    protected Button loginButton;
    protected Button registerButton;
    private boolean loginOnly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        accountManager = AccountManager.get(LoginActivity.this);

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

        if(comesFromRegistration()) {
            loginRegisteredUser();
        }
    }

    private void loginRegisteredUser() {
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            User user = (User) extras.get("registeredUser");
            Log.d(TAG, "trying to login existing User: " + user);
            login(user.getUsername(), user.getPassword());
        }
    }

    private boolean comesFromRegistration() {
        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("register")) {
            return((Boolean)extras.get("register"));
        }
        return false;
    }

    protected void register(String username, String password) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("password", password);
        startActivity(intent);
        finish();
    }


    protected void login(String username, String password) {
        if (isNotEmpty(username) && isNotEmpty(password) && authenticate(username, password)) {
            Log.d(TAG, "creating Application Account on device");
            createApplicationAccount(username, password);
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            finish();
        } else {
            Log.d(TAG, "login failed for username/password: " +username + "/" + password);
            error.setText(getString(R.string.LOGIN_FAILED_LABEL));
            error.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
            error.setVisibility(View.VISIBLE);
        }
    }



    protected boolean authenticate(String username, String password) {
        return ((PartyManagerApplication)getApplication()).authenticate(username, password);
    }

    protected void setLoginButtonVisible(boolean visible) {
        if (visible) {
            loginButton.setVisibility(View.VISIBLE);
        } else {
            loginButton.setVisibility(View.GONE);
        }
    }

    protected boolean isNotEmpty(String value) {
        if (value != null && value.length()>0) {
            return true;
        }
        return false;
    }

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

    private void createApplicationAccount(String username, String password) {
        final Account account = new Account(username, AccountAuthenticator.AUTH_TYPE);
        Account[] accountsByType = accountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
        if(accountsByType.length==0) {
            Log.d(TAG, "adding new device Account: " + account);
            Bundle userData = new Bundle();
            userData.putString("password", password);
            accountManager.addAccountExplicitly(account, password, userData);
        } else if(accountsByType.length == 1) {
            Log.d(TAG, "account already exists on device -> using this account");
        } else {
            Log.d(TAG, "multiple accounts for this application exists: " + accountsByType.length);
        }
    }

}
