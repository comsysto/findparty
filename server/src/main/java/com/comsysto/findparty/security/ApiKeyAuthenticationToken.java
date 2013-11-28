package com.comsysto.findparty.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * User: rpelger
 * Date: 28.11.13
 */
public class ApiKeyAuthenticationToken implements Authentication {

    private String apiKey;
    private String credentials;
    private boolean isAuthenticated = false;


    public ApiKeyAuthenticationToken(String apiKey) {
        this.apiKey = apiKey;
    }

    public ApiKeyAuthenticationToken(String apiKey, String credentials) {
        this.apiKey = apiKey;
        this.credentials = credentials;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return getCredentials();
    }

    @Override
    public Object getPrincipal() {
        return apiKey;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = true;
    }

    @Override
    public String getName() {
        return getCredentials().toString();
    }
}
