package com.comsysto.dalli.android.account;

import android.accounts.*;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.comsysto.dalli.android.R;
import com.comsysto.dalli.android.application.Constants;
import com.comsysto.dalli.android.model.UserAccount;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * User: rpelger
 * Date: 08.03.13
 */
public class AccountService {

    private final static String TAG = Constants.LOG_PREFIX + AccountService.class.getSimpleName();

    private Context context;

    private AccountManager accountManager;

    private AccountManagerFuture<Bundle> myFuture = null;
    private String token = null;
    private AccountManagerCallback<Bundle> myCallback = new AccountManagerCallback<Bundle>() {
        @Override public void run(final AccountManagerFuture<Bundle> arg0) {
            try {
                token = (String) myFuture.getResult().get(AccountManager.KEY_AUTHTOKEN); // this is your auth token
            } catch (Exception e) {
                Log.e(TAG, "Fehler beim Laden des Auth-Tokens: " + e.getMessage(), e);
            }
        }
    };
    private boolean receivedResult = false;

    public AccountService(Context applicationContext) {
        this.context = applicationContext;
        this.accountManager = AccountManager.get(context);
    }

    public boolean hasAccount() {
        return getAccount()!=null;
    }

    public String getAuthenticationToken() {
        if(!hasAccount()) {
            return null;
        }

        Account account = getAccount();
        String username = account.name;
        String password = accountManager.getPassword(account);
        String token = username + ":" + password;
        String encoded = new String(Base64.encode(token.getBytes(), Base64.NO_WRAP));
        return "Basic " + encoded;
    }

    public String getUsername() {
        if(hasAccount())
            return getAccount().name;
        return null;
    }

    private Account getAccount() {
        Account[] accounts = accountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
        if(accounts.length > 0) {
            return accounts[0];
        }
        Log.w(TAG, "No user account managed by device for Application: " + AccountAuthenticator.AUTH_TYPE);
        return null;
    }

    /**
     * Stores new user account on device. Deletes any previously existing accounts
     * @param username
     * @param password
     */
    public void createAccount(String username, String password) {
        Account[] accounts = accountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
        if(accounts.length > 0) {
            remove(accounts);
        }
        addAccount(username, password);
    }

    /**
     * Removes all accounts for the application
     */
    public void removeAll() {
        Account[] accounts = accountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
        remove(accounts);
    }

    private void addAccount(String username, String password) {
        Account account = new Account(username, AccountAuthenticator.AUTH_TYPE);
        Bundle userdata = new Bundle();
        userdata.putLong("timestamp", new Date().getTime());
        accountManager.addAccountExplicitly(account, password, userdata);
    }

    private void remove(Account[] accounts) {
        for(final Account account : accounts) {
            accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                @Override
                public void run(AccountManagerFuture<Boolean> future) {
                    Toast toast = Toast.makeText(context, "removed account: " + account.name, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }, new Handler());
        }


    }


}
