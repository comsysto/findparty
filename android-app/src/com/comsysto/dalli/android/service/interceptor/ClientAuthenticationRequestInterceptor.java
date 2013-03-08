package com.comsysto.dalli.android.service.interceptor;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.util.Base64;
import android.util.Log;
import com.comsysto.dalli.android.application.Constants;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.dalli.android.authentication.AccountAuthenticator;
import com.comsysto.findparty.User;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.w3c.dom.html.HTMLOptGroupElement;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * User: rpelger
 * Date: 20.11.12
 * Time: 12:04
 */
public class ClientAuthenticationRequestInterceptor implements ClientHttpRequestInterceptor {

    private final static String TAG = Constants.LOG_AUTH_PREFIX + ClientAuthenticationRequestInterceptor.class.getSimpleName();

    private PartyManagerApplication application;

    public ClientAuthenticationRequestInterceptor(PartyManagerApplication application) {
        this.application = application;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        Log.d(TAG, this.getClass().getSimpleName() + ": intercepting Request: " + httpRequest.getMethod() + " " + httpRequest.getURI());

        User user = application.getUser();

        if(isValid(user)) {
            Log.d(TAG, "Adding Basic Authentication Header for User: " + user);
            String authorization = createAuthorizationHeader(user);

            httpRequest.getHeaders().add("Authorization", authorization);
        }
        else {
            Log.d(TAG, "No user logged in to Application yet");
        }

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }

    /*
    private User getUser() {
        Log.d(TAG, "loading user from device account...");
        AccountManager accountManager = AccountManager.get(application);
        Account[] accounts = accountManager.getAccountsByType(AccountAuthenticator.AUTH_TYPE);
        if(accounts.length > 0) {
            User user = new User();
            Account account = accounts[0];
            user.setUsername(account.name);
            user.setPassword(accountManager.getPassword(account));
            Log.d(TAG, "User loaded successfully from device account: " + user);
            return user;
        }
        Log.d(TAG, "no User connected to device account (yet)");
        return null;
    }
    */
    private String createAuthorizationHeader(User user) throws UnsupportedEncodingException {
        String token = user.getUsername() + ":" + user.getPassword();

        //needs to be NO_WRAP
        String encoded = new String(Base64.encode(token.getBytes("UTF-8"), Base64.NO_WRAP));

        return "Basic " + encoded;
    }

    private boolean isValid(User user) {
        try {
            Assert.notNull(user, "user must not be null");
            Assert.notNull(user.getUsername(), "username must not be null");
            Assert.notNull(user.getPassword(), "password must not be null");
            return true;
        } catch (IllegalArgumentException e) {
            Log.d(TAG, "invalid or no user for Basic-Authentication: " + e.getMessage());
            return false;
        }
    }

}
