package com.comsysto.findbuddies.android.service.interceptor;

import android.util.Log;
import com.comsysto.findbuddies.android.application.Constants;
import com.comsysto.findbuddies.android.application.PartyManagerApplication;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * User: rpelger
 * Date: 20.11.12
 * Time: 12:04
 */
public class ClientAuthenticationRequestInterceptor implements ClientHttpRequestInterceptor {

    private final static String TAG = Constants.LOG_AUTH_PREFIX + ClientAuthenticationRequestInterceptor.class.getSimpleName();

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        Log.d(TAG, this.getClass().getSimpleName() + ": intercepting Request: " + httpRequest.getMethod() + " " + httpRequest.getURI());
        httpRequest.getHeaders().add("api-key", PartyManagerApplication.getApiKey());
        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }

}
