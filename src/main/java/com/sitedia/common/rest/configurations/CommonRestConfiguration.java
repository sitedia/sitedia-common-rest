package com.sitedia.common.rest.configurations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = "com.sitedia.common.rest")
@EntityScan(basePackages = "com.sitedia.common.rest.entities")
@PropertySource("classpath:/com/sitedia/common/rest/properties/common.properties")
public class CommonRestConfiguration {

}
