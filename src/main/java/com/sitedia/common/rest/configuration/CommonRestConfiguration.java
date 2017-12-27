package com.sitedia.common.rest.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.sitedia.common.rest.utils.InitializingBean;

@Configuration
@ComponentScan(basePackages = "com.sitedia.common.rest")
@EntityScan(basePackages = "com.sitedia.common.rest.entity")
public class CommonRestConfiguration {

    @Value("${server.allowed-paths}")
    private String allowedPaths;

    @Value("${security.auth.server.usersByUsernameQuery}")
    private String usersByUsernameQuery;

    @Value("${security.auth.server.authoritiesByUsernameQuery}")
    private String authoritiesByUsernameQuery;

    @Value("${security.salt}")
    private String salt;

    @Bean
    public InitializingBean init() {
	InitializingBean init = new InitializingBean();

	init.setBasePackage("com.sitedia");
	init.setAllowedPaths(allowedPaths);
	init.setUsersByUsernameQuery(usersByUsernameQuery);
	init.setAuthoritiesByUsernameQuery(authoritiesByUsernameQuery);
	init.setSalt(salt);

	return init;
    }

}
