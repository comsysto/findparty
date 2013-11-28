package com.comsysto.findparty.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Looks for an 'api-key' header in request. If exists, creates an AuthenticationToken from it and authenticates it via
 * the authentication manager.
 *
 * User: rpelger
 * Date: 28.11.13
 */
public class ApiKeyAuthenticationFilter extends GenericFilterBean {

    private String apiKeyHeader = "api-key";

    private AuthenticationManager authenticationManager;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            Authentication authentication = doAuthentication((HttpServletRequest) request);
            successfulAuthentication(authentication);
        } catch (AuthenticationException e) {
            unsuccessfulAuthentication((HttpServletResponse) response, e);
            throw e;
        }

        chain.doFilter(request, response);
    }

    private void unsuccessfulAuthentication(HttpServletResponse response, AuthenticationException e) {
        SecurityContextHolder.clearContext();
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    private void successfulAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication doAuthentication(HttpServletRequest request) {
        String apiKey = getApiKey((HttpServletRequest) request);
        return authenticationManager.authenticate(new ApiKeyAuthenticationToken(apiKey));
    }

    protected String getApiKey(HttpServletRequest request) {
        return request.getHeader(apiKeyHeader);
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void setApiKeyHeader(String apiKeyHeader) {
        this.apiKeyHeader = apiKeyHeader;
    }
}
