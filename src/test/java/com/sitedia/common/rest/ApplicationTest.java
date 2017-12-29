package com.sitedia.common.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.sitedia.common.rest.annotations.EnableCommonRest;

/**
 * Application launcher
 * 
 * @author sitedia
 *
 */
@SpringBootApplication
@EnableCommonRest
public class ApplicationTest {

    private static ConfigurableApplicationContext applicationContext;

    /**
     * Private constructor
     */
    protected ApplicationTest() {
        super();
    }

    /**
     * Launcher
     * 
     * @param args
     *            input parameters
     */
    public static void main(String[] args) {
        applicationContext = SpringApplication.run(ApplicationTest.class, args);
    }

    /**
     * Stops the application
     */
    public static void close() {
        applicationContext.close();
    }

}
