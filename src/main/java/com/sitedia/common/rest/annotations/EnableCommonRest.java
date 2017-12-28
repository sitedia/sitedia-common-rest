package com.sitedia.common.rest.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.sitedia.common.rest.configuration.CommonRestConfiguration;

/**
 * Indicates that this common REST starter should be enabled.
 * @author sitedia
 *
 */
@Retention(value = java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(value = { java.lang.annotation.ElementType.TYPE })
@Import({ CommonRestConfiguration.class })
public @interface EnableCommonRest {
	
}
