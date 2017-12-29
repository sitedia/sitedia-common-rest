package com.sitedia.common.rest;

import org.junit.Assert;
import org.junit.Test;

import com.sitedia.common.rest.dto.DefaultUserDTO;

/**
 * Site test
 * 
 * @author cedric
 *
 */
public class SessionControllerTest extends AbstractTest {

    @Test
    public void tesGetUserInfo() throws Exception {
        String cookie = login("admin@localhost", "admin123", 200);

        DefaultUserDTO user = get("http://localhost:8090/api/v1.0/sessions/userInfo", DefaultUserDTO.class, cookie, 200);
        Assert.assertEquals("admin@localhost", user.getUsername());
    }

}
