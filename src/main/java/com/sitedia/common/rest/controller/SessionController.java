package com.sitedia.common.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.sitedia.common.rest.exception.BusinessException;
import com.sitedia.common.rest.exception.TechnicalException;
import com.sitedia.common.rest.service.IUserService;

@RestController
@RequestMapping(produces = "application/json;charset=UTF-8")
public class SessionController {

    private static final String ANONYMOUS_USER = "anonymous";

    @Lazy
    @Autowired
    private IUserService userService;

    /**
     * Returns the information of the connected user
     * @param authentication
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @RequestMapping(path = "/api/v1.0/sessions/userInfo", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public JsonNode get(Authentication authentication) throws BusinessException, TechnicalException {
        if (authentication == null || authentication.getName().equals(ANONYMOUS_USER)) {
            throw new BusinessException("Access forbidden");
        }
        JsonNode user = userService.getUserByName(authentication.getName());
        if (user == null) {
            throw new BusinessException("Access forbidden");
        }

        return user;
    }

}
