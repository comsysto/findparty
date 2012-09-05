package com.comsysto.dalli.android.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.application.TaskManagerApplication;
import com.comsysto.dalli.android.authentication.AccountAuthenticator;

/**
 * Displays the login page.
 * 
 * @author stefandjurasic
 *
 */
public class LoginActivity extends AccountAuthenticatorActivity {

	public static final String PARAM_AUTHTOKEN_TYPE = "authtokenType";
	boolean loggedIn = false;
	private EditText userName;
	private EditText password;
	private AccountManager accountManager;
	private TextView error;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);


		Button button = (Button) findViewById(R.id.LOGIN_BUTTON);
		userName = (EditText)findViewById(R.id.editText1);
		password = (EditText)findViewById(R.id.editText2);
		
		OnTouchListener fadeOutOnTouchListener = new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
	    		error.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this, android.R.anim.fade_out));
	    		error.setVisibility(View.INVISIBLE);
	    		return false;
			}
		};
		userName.setOnTouchListener(fadeOutOnTouchListener);
		password.setOnTouchListener(fadeOutOnTouchListener);
		
		accountManager = AccountManager.get(LoginActivity.this);
		error = (TextView)findViewById(R.id.error);		
		
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finishCreateAccount();
			}
		});
	}


    private void finishCreateAccount() {
    	if (isNotEmpty(userName) && isNotEmpty(password) && createUserInBackend() && createAccountExplicitly()){
    		Intent intent = new Intent(this, StartActivity.class);
    		startActivity(intent);        
    		finish();
    	}
    	else {
    		error.setText("Could not authenticate. Check values!");
    		error.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_in));
    		error.setVisibility(View.VISIBLE);
    	}
    }


	private boolean createUserInBackend() {
		if (((TaskManagerApplication)getApplication()).createAccount(userName.getText().toString(), password.getText().toString()) != null) {
			return true;
		}
		else {
			return false;
		}
	}


	private boolean isNotEmpty(EditText userName) {
		if (userName == null || userName.getText() == null || userName.getText().toString().length() == 0) {
			return false;
		}
		return true;
	}


	private boolean createAccountExplicitly() {
        final Account account = new Account(userName.getText().toString(), AccountAuthenticator.AUTH_TYPE);
        return accountManager.addAccountExplicitly(account, password.getText().toString(), null);
	}

}
