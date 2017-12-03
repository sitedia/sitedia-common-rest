package com.sitedia.common.rest.configuration;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InitializingBean {

    /**
     * Base packages to scan for Swagger. Example: com.sitedia
     */
    private String basePackage;

    /**
     * Allowed paths to the endpoint. All others paths will be rejected by the
     * application
     */
    private String allowedPaths;

    /**
     * Spring request to retrieve users
     */
    private String usersByUsernameQuery;

    /**
     * Spring request to retrieve authorities
     */
    private String authoritiesByUsernameQuery;

    /**
     * Salt code for SHA521 encoding
     */
    private String salt;

}
