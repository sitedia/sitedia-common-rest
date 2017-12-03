package com.sitedia.common.rest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Indicates that Hibernate validation should use Spring i18n files
 * @author cedric
 *
 */
@Configuration
public class ValidationConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private MessageSource messageSource;

    /**
     * Use Spring files for i18n validation
     */
    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource);
        return factory;
    }

}
