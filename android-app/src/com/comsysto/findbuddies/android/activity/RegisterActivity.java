package com.comsysto.findbuddies.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.comsysto.findbuddies.android.application.Constants;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;

/**
 * User: rpelger
 * Date: 21.11.12
 */
public class RegisterActivity extends LoginActivity {


    private static final String TAG = Constants.LOG_SERVICE_PREFIX + RegisterActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setLoginButtonVisible(false);

        setInputValues();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(userName, password);
            }
        });

    }

    private void register(EditText usernameInput, EditText passwordInput) {
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();

        Log.d(TAG, "creating new User on Server: " + username+"/"+password);
        boolean registered = createNewUser(username, password);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("register", registered);

        startActivity(intent);
    }

    private boolean createNewUser(String username, String password) {
        try {
            getPartyManagerApplication().getPartyService().createUser(username, password);
            getPartyManagerApplication().getAccountService().createAccount(username, password);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Fehler beim Anlegen des Users: " +username+"/"+password + " :" +e.getMessage(), e);
            return false;
        }

    }

    private PartyManagerApplication getPartyManagerApplication() {
        return ((PartyManagerApplication) getApplication());
    }

    private void setInputValues() {
        String inputUsername = (String) getIntent().getExtras().get("username");
        String inputPassword = (String) getIntent().getExtras().get("password");
        if(isEmpty(inputUsername)) {
            userName.setText(inputUsername);
        }
        if(isEmpty(inputPassword)) {
            password.setText(inputPassword);
        }
    }
}
