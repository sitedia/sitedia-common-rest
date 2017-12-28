package com.sitedia.common.rest.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.sitedia.common.rest")
@EntityScan(basePackages = "com.sitedia.common.rest.entity")
@PropertySource("classpath:com/sitedia/common/rest/properties/common-rest.properties")
public class CommonRestConfiguration {

}
