package com.sitedia.common.rest.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * Returns a OK message after login success
 * 
 * @author sitedia
 *
 */
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        // Log fail
        Logger.getLogger("security.failure").warning(String.format("Authentication failed: %s", exception.getMessage()));
        Logger.getLogger("security.authentication").log(Level.FINE, "Authentication failed", exception);
    }

}
