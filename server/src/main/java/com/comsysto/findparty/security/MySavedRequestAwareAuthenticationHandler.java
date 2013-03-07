package com.comsysto.findparty.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: tim.hoheisel
 * Date: 06.03.13
 * Time: 13:34
 * To change this template use File | Settings | File Templates.
 */
public class MySavedRequestAwareAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        super.onAuthenticationSuccess(request, response, authentication);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
