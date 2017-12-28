package com.sitedia.common.rest.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "sitedia")
@Validated
public class CommonRestProperties {

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Security {

        private String allowedPaths = "/**";

    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Swagger {

        private String basePackage = "com.sitedia";

    }

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Auth {

        private String usersByUsernameQuery = "select mail as username, password, true from MYTODOLIST.users where mail = ?";

        private String authoritiesByUsernameQuery = "select mail as username, role as authority from MYTODOLIST.users where mail = ?";

        private String salt = "";

    }

}
