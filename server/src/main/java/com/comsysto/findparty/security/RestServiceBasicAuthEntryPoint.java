package com.comsysto.findparty.security;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: rpelger
 * Date: 06.03.13
 *
 * Sets the response status UNAUTHORIZED (401) in response header only.
 * Any Cookies get deleted and no response body or anything is transmitted.
 */
public class RestServiceBasicAuthEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOG = Logger.getLogger(RestServiceBasicAuthEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOG.info("Access Denied for REST-Service (" + authException.getMessage() + ")");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
