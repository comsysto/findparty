package com.comsysto.findparty.exceptions;

import org.springframework.security.core.AuthenticationException;

/**
 * User: rpelger
 * Date: 28.11.13
 */
public class ApiKeyAuthenticationException extends AuthenticationException {

    public ApiKeyAuthenticationException(String msg) {
        super(msg);
    }
}
