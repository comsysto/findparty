package com.comsysto.findparty.security;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;

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
public class RestServiceAccessDeniedEntryPoint extends Http403ForbiddenEntryPoint {

    private static final Logger LOG = Logger.getLogger(RestServiceAccessDeniedEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        LOG.info("Access Denied for REST-Service (" + authException.getMessage() + ")");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        response.setContentLength(0);
    }
}
