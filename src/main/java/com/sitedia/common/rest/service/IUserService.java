package com.sitedia.common.rest.service;

import com.sitedia.common.rest.exception.BusinessException;
import com.sitedia.common.rest.exception.TechnicalException;

/**
 * User service interface for security components
 * 
 * @author sitedia
 *
 */
public interface IUserService {

    /**
     * Returns a JSON representing the user with the given name
     * 
     * @param name
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    public Object getByUsername(String username) throws BusinessException, TechnicalException;

}
