package com.comsysto.findbuddies.android.account;

import android.accounts.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import com.comsysto.findbuddies.android.activity.signin.DefaultSigninActivity;
import com.comsysto.findbuddies.android.application.Constants;

/**
 * Handles requests from the app for login or creating accounts.
 * 
 * Currently just creates a new account from given values from LoginPage without
 * checking anything.
 * 
 * @author stefandjurasic
 *
 */
public class AccountAuthenticator extends AbstractAccountAuthenticator {

    private final static String TAG = Constants.LOG_AUTH_PREFIX + AccountAuthenticator.class.getSimpleName();

    public static final String AUTH_TYPE = "com.comsysto.account.findbuddies";
    public static final String AUTH_TOKEN_TYPE = "Basic";

    private Context context;
	
	public AccountAuthenticator(Context context) {
		super(context);
		this.context = context;
		Log.d(TAG, "AccountAuthenticator created");
	}

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options) throws NetworkErrorException {

        final Intent intent = new Intent(context, DefaultSigninActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {

        Bundle bundle = new Bundle();

        if(account==null) {
            Log.w(TAG, "no account specified -> can't create Auth-Token");
            bundle.putBoolean("ACCOUNT_EXISTS", false);
        }
        else {
            bundle.putBoolean("ACCOUNT_EXISTS", false);
            if(authTokenType!=null && authTokenType.equals(AUTH_TOKEN_TYPE)) {
                String username = account.name;
                String password = AccountManager.get(context).getPassword(account);
                String token = username+":"+password;
                String value = "Basic " + new String(Base64.encode(token.getBytes(), Base64.NO_WRAP));
                Log.d(TAG, "created " + AUTH_TOKEN_TYPE + " Token: " + value);

                bundle.putBoolean("TOKEN_CREATED", true);
                bundle.putString(AccountManager.KEY_AUTHTOKEN, value);
                bundle.putString(AccountManager.KEY_ACCOUNT_NAME, username);
                bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, AUTH_TOKEN_TYPE);
            }
            bundle.putBoolean("TOKEN_CREATE", false);
        }
		return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		// TODO Auto-generated method stub
		return null;
	}

}
