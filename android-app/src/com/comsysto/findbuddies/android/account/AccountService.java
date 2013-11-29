package com.comsysto.findbuddies.android.account;

import android.accounts.*;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;
import com.comsysto.findbuddies.android.R;
import com.comsysto.findbuddies.android.application.Constants;

import java.util.Date;

/**
 * User: rpelger
 * Date: 08.03.13
 */
public class AccountService {

    private final static String TAG = Constants.LOG_PREFIX + AccountService.class.getSimpleName();

    private Context context;
    private AccountManager accountManager;

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

        String username = "pelgero@gmail.com";
        String password = "test";
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
        if(accounts!=null && accounts.length > 0) {
            return accounts[0];
        }
        Log.w(TAG, "No user account managed by device for Application: " + AccountAuthenticator.AUTH_TYPE);
        return null;
    }

    /**
     * Stores new user account on device. Deletes any previously existing accounts
     * @param username
     * @param userImageUrl
     */
    public void createAccount(String username, String userImageUrl) {
        Account[] accounts = accountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
        if(accounts.length > 0) {
            remove(accounts);
        }
        addAccount(username, userImageUrl);
    }

    /**
     * Removes all accounts for the application
     */
    public void removeAll() {
        Account[] accounts = accountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
        remove(accounts);
    }

    private void addAccount(String username, String userImageUrl) {
        Account account = new Account(username, AccountAuthenticator.AUTH_TYPE);
        Bundle userBundle = new Bundle();
        userBundle.putLong("timestamp", new Date().getTime());
        userBundle.putString("userImage", userImageUrl);

        Log.i(TAG, "Adding account for username: '"+username+"' with imageUrl: '"+userImageUrl+"'");

        accountManager.addAccountExplicitly(account, null, userBundle);
    }

    public String getUserImageUrl(){
        return accountManager.getUserData(getAccount(), "userImage");
    }

    private void remove(Account[] accounts) {
        for(final Account account : accounts) {
            accountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                @Override
                public void run(AccountManagerFuture<Boolean> future) {
                    Toast toast = Toast.makeText(context, context.getString(R.string.REMOVED_ACCOUNT) + account.name, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }, new Handler());
        }


    }


}
