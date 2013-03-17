package com.comsysto.findbuddies.android.service.interceptor;

import android.util.Log;
import com.comsysto.findbuddies.android.application.Constants;
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
public class NoCacheClientRequestInterceptor implements ClientHttpRequestInterceptor {

    private final static String TAG = Constants.LOG_SERVICE_PREFIX + NoCacheClientRequestInterceptor.class.getSimpleName();

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        Log.d(TAG, "Setting no-cache headers for request: " + httpRequest.getMethod() + " " + httpRequest.getURI());
        httpRequest.getHeaders().add("Pragma","No-cache");
        httpRequest.getHeaders().add("Cache-Control","max-age=0, no-cache, no-store");
        httpRequest.getHeaders().add("Expires",  "0");

        return clientHttpRequestExecution.execute(httpRequest, bytes);
    }

}
