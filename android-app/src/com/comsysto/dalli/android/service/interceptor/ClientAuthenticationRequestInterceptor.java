package com.comsysto.dalli.android.service.interceptor;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import com.comsysto.dalli.android.application.Constants;
import com.comsysto.dalli.android.application.PartyManagerApplication;
import com.comsysto.findparty.User;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;

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


        if(application.getAccountService().hasAccount()) {
            String authorization = application.getAccountService().getAuthenticationToken();
            Log.d(TAG, "Adding Basic Authentication Header for User: " + application.getAccountService().getUsername() + " -> " + authorization);
            httpRequest.getHeaders().add("Authorization", authorization);
        }
        else {
            Log.d(TAG, "No user logged in to Application yet");
        }

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }


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
