package com.sitedia.common.rest.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.sitedia.common.rest.configuration.CommonRestConfiguration;

@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Import({ CommonRestConfiguration.class })
public @interface EnableCommonRest {
}
