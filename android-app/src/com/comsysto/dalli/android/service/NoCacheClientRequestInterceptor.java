package com.comsysto.dalli.android.service;

import android.util.Log;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: rpelger
 * Date: 20.11.12
 * Time: 12:04
 * To change this template use File | Settings | File Templates.
 */
public class NoCacheClientRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        httpRequest.getHeaders().add("Pragma","No-cache");
        httpRequest.getHeaders().add("Cache-Control","max-age=0, no-cache, no-store");
        httpRequest.getHeaders().add("Expires",  "0");

        ClientHttpResponse response = clientHttpRequestExecution.execute(httpRequest, bytes);
        Log.i("MY_PARTIES_REQ_RES", "Server-Response ("+httpRequest.getMethod()+" -> " + httpRequest.getURI()+"): " +response.getStatusCode());
        return response;
    }

}
