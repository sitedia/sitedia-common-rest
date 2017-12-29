package com.sitedia.common.rest.utils;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.sitedia.common.rest.dto.StatusDTO;

/**
 * Returns a OK message after login success
 * 
 * @author sitedia
 *
 */
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Returns a OK message after login success
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // Log success
        Logger.getLogger("security.authentication").info(String.format("Authentication succeeded for %s", authentication.getName()));

        // Return response
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(JsonUtils.toString(new StatusDTO("OK", "Logged in successfully", null)));
        response.getWriter().flush();
        response.getWriter().close();
    }

}
