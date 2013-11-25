package com.comsysto.findbuddies.android.activity.signin;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.account.AccountService;
import com.comsysto.findbuddies.android.activity.StartActivity;
import com.comsysto.findbuddies.android.application.Constants;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import com.comsysto.findparty.User;
import com.comsysto.findparty.web.PartyService;

/**
 * User: rpelger
 * Date: 05.06.13
 */
public class CreateAccountActivity extends Activity implements View.OnClickListener {

    private static final String TAG = Constants.LOG_PREFIX + CreateAccountActivity.class.getSimpleName();

    private EditText usernameText;
    private EditText passwordText;

    private Button actionButton;

    private TextView helpText;

    private PartyManagerApplication partyManagerApplication;
    private AccountService accountService;

    ProgressDialog createUserDialog;

    private User existingUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //this is the google+ signin username
        String username = (String) getIntent().getExtras().get("ACCOUNT_NAME");

        //get the party service
        partyManagerApplication = ((PartyManagerApplication) getApplication());
        accountService = ((PartyManagerApplication) getApplication()).getAccountService();

        existingUser = partyManagerApplication.getUser(username);

        usernameText = (EditText) findViewById(R.id.usernameText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        helpText = (TextView) findViewById(R.id.helptext);
        actionButton = (Button) findViewById(R.id.loginButton);
        createUserDialog = new ProgressDialog(this);

        actionButton.setOnClickListener(this);

        if(existingUser==null) {
            createUserDialog.setMessage("Creating user account...");
            usernameText.setText(username);
            passwordText.setText("");
            helpText.setText("We create a FindBuddies account for you, using your Google+ username and a password you provide. The password must not necessarily be the same as your Google+ password");

            //cannot be modified, because we want Google+ account name as fixed value
            usernameText.setEnabled(false);

        } else {
            usernameText.setText(existingUser.getUsername());
            passwordText.setText(existingUser.getPassword());
            createUserDialog.setMessage("Existing user found for username '" + existingUser.getUsername()+"'.\nLogging in...");
            helpText.setVisibility(View.INVISIBLE);
            usernameText.setEnabled(false);
            passwordText.setEnabled(false);
            actionButton.performClick();
        }


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.loginButton) {
            String username = usernameText.getText().toString();
            String password = passwordText.getText().toString();

            if(existingUser!=null) {
                loginExistingUser();
            } else {
                createNewAccount(username, password);
            }
        }
    }

    private void createNewAccount(String username, String password) {
        if(password == null || password.equals("")) {
            Toast.makeText(this, "Please specify a valid password!", Toast.LENGTH_LONG).show();
        } else {
            Log.i(TAG, "Creating new User Account on server: " + username + "/" + password);
            createUser(username, password);
        }
    }

    private void loginExistingUser() {
        accountService.createAccount(existingUser.getUsername(), existingUser.getPassword());
        Intent start = new Intent(CreateAccountActivity.this, StartActivity.class);
        createUserDialog.dismiss();
        startActivity(start);
    }

    private void createUser(String username, String password) {

        AsyncTask<String, Void, User> createUserTask = new AsyncTask<String, Void, User>() {
            @Override
            protected User doInBackground(String... params) {
                return partyManagerApplication.createUser(params[0], params[1]);
            }

            @Override
            protected void onPostExecute(User user) {
                Log.i(TAG, "Creating account on device");
                accountService.createAccount(user.getUsername(), user.getPassword());
                Intent start = new Intent(CreateAccountActivity.this, StartActivity.class);
                createUserDialog.dismiss();
                startActivity(start);
            }
        };
        createUserTask.execute(new String[]{username, password});
        createUserDialog.show();
    }
}
