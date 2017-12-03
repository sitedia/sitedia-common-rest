package com.sitedia.common.rest.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sitedia.common.rest.exception.BusinessException;
import com.sitedia.common.rest.exception.TechnicalException;

/**
 * User service interface for security components
 * @author sitedia
 *
 */
public interface IUserService {

    /**
     * Returns a JSON representing the user with the given name
     * @param name
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    JsonNode getUserByName(String name) throws BusinessException, TechnicalException;

}
