package com.comsysto.dalli.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.findparty.User;

/**
 * Created with IntelliJ IDEA.
 * User: rpelger
 * Date: 21.11.12
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class RegisterActivity extends LoginActivity {


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

    private void register(EditText userName, EditText password) {
        User user = ((PartyManagerApplication) getApplication()).createAccount(userName.getText().toString(), password.getText().toString());

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("register", true);
        intent.putExtra("registeredUser", user);
        startActivity(intent);
    }

    private void setInputValues() {
        String inputUsername = (String) getIntent().getExtras().get("username");
        String inputPassword = (String) getIntent().getExtras().get("password");
        if(isNotEmpty(inputUsername)) {
            userName.setText(inputUsername);
        }
        if(isNotEmpty(inputPassword)) {
            password.setText(inputPassword);
        }
    }
}
