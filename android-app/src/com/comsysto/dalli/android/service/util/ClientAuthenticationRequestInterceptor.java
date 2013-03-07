package com.comsysto.dalli.android.service.util;

import android.util.Base64;
import android.util.Log;
import com.comsysto.dalli.android.application.Constants;
import com.comsysto.findparty.User;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * User: rpelger
 * Date: 20.11.12
 * Time: 12:04
 */
public class ClientAuthenticationRequestInterceptor implements ClientHttpRequestInterceptor {

    private final static String TAG = Constants.LOG_AUTH_PREFIX + ClientAuthenticationRequestInterceptor.class.getSimpleName();

    private User user;

    public ClientAuthenticationRequestInterceptor(User user) {
        this.user = user;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        if(isValid(user)) {
            Log.d(TAG, "Adding Authentication Header to client Request: " + httpRequest.getMethod() + " " + httpRequest.getURI());

            String authorization = createAuthorizationHeader();

            httpRequest.getHeaders().add("Authorization", authorization);
        }
        else {
            Log.d(TAG, "No user logged in to Application yet");
        }

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }

    private String createAuthorizationHeader() throws UnsupportedEncodingException {
        String token = user.getUsername() + ":" + user.getPassword();

        //needs to be NO_WRAP
        String encoded = new String(Base64.encode(token.getBytes("UTF-8"), Base64.NO_WRAP));

        return "Basic " + encoded;
    }

    private boolean isValid(User user) {
        return user!=null && user.getUsername()!=null && user.getPassword()!=null;
    }

}
