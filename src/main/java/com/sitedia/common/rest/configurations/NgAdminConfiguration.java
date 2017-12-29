package com.sitedia.common.rest.configurations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@ConditionalOnProperty(name = "sitedia.ngadmin.enabled", havingValue = "true")
public class NgAdminConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/ng-admin/**").addResourceLocations("classpath:com/sitedia/common/rest/ngadmin/").setCachePeriod(86400);
    }

}
