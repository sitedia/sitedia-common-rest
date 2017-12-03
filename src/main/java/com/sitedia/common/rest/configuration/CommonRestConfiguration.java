package com.sitedia.common.rest.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import com.sitedia.common.rest.exception.BusinessException;

/**
 * Common REST starter configuration
 * @author sitedia
 *
 */
@ComponentScan("com.sitedia.common.rest")
public class CommonRestConfiguration {

    @Bean
    public InitializingBean init() throws BusinessException {
        throw new BusinessException("Initializing bean not found. Please create it before launching the application.");
    }

}
