package com.sitedia.common.rest.utils;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

/**
 * Security functions for the application
 * 
 * @author cedric
 *
 */
public class SecurityUtils {

    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * Private constructor
     */
    private SecurityUtils() {
        // Static class
    }

    /**
     * Checks if the user is full admin
     * 
     * @param authentication
     * @return
     */
    public static boolean isAdmin(Authentication authentication) {

        // Check authentication
        if (authentication == null || !isAuthenticated(authentication)) {
            return false;
        }

        // Search for ROLE_ADMIN
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        for (GrantedAuthority role : roles) {
            if (role.getAuthority().equals(ROLE_ADMIN)) {
                return true;
            }
        }

        // Admin role not found
        return false;
    }

    /**
     * Checks that the authentication is filled
     * 
     * @param authentication
     */
    private static boolean isAuthenticated(Authentication authentication) {
        return authentication != null && authentication.getAuthorities() != null && authentication.getName() != null && !authentication.getName().equals("anonymousUser");
    }

    /**
     * Returns the name of the connected user
     * 
     * @param authentication
     * @return
     */
    public static String getUsername(Authentication authentication) {
        return authentication != null && authentication.getName() != null ? authentication.getName() : null;
    }

}
