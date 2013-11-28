package com.comsysto.findparty.security;

import com.comsysto.findparty.exceptions.ApiKeyAuthenticationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Map;

/**
 * User: rpelger
 * Date: 28.11.13
 */
public class ApiKeyAuthenticationProvider implements AuthenticationProvider{

    private Map<String, String> apiKeys;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = (String) authentication.getPrincipal();
        if(apiKey == null) {
            throw new BadCredentialsException("No API-Key found in request header!");
        }

        if(!apiKeys.containsKey(apiKey)) {
            throw new ApiKeyAuthenticationException("Invalid API-Key found in request header: " + apiKey);
        }

        Authentication result = new ApiKeyAuthenticationToken(apiKey, apiKeys.get(apiKey));
        authentication.setAuthenticated(true);
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return ApiKeyAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void setApiKeys(Map<String, String> apiKeys) {
        this.apiKeys = apiKeys;
    }
}
