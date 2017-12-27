package com.sitedia.common.rest;

import org.junit.Before;
import org.junit.Test;

/**
 * Site test
 * @author cedric
 *
 */
public class UserControllerTest extends AbstractTest {

    private String baseAPI;

    @Before
    public void setUp() {
        baseAPI = "http://localhost:" + serverPort + "/api/users";
    }
    
    @Test
    public void testCreate() {
    	
    }

}
