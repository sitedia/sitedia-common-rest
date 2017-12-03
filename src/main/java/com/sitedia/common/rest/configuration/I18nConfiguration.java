package com.sitedia.common.rest.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.sitedia.common.rest.utils.UriLocaleResolver;

/**
 * I18n configuration
 * @author sitedia
 *
 */
@Configuration
public class I18nConfiguration extends WebMvcConfigurerAdapter {

    /**
     * Define the i18n files to use in this library
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.getBasenameSet().add("classpath:CommonRestMessages");
        return messageSource;
    }

    /**
     * Use API uri to define the user locale
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        return new UriLocaleResolver();
    }

}
