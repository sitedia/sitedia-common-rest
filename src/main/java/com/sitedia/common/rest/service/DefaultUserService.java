package com.sitedia.common.rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.sitedia.common.rest.dao.DaoManager;
import com.sitedia.common.rest.entity.DefaultUserEntity;
import com.sitedia.common.rest.exception.BusinessException;
import com.sitedia.common.rest.exception.TechnicalException;
import com.sitedia.common.rest.mapper.DefaultUserMapper;

/**
 * User service interface for security components
 * 
 * @author sitedia
 *
 */
@Lazy
@Service
public class DefaultUserService implements IUserService {

    @Autowired
    private DaoManager daoManager;

    @Autowired
    private DefaultUserMapper mapper;

    /**
     * Returns a JSON representing the user with the given name
     * 
     * @param name
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @Override
    public Object getByUsername(String username) throws BusinessException, TechnicalException {
	DefaultUserEntity result = daoManager.get(DefaultUserEntity.class, username);
	return mapper.toDTO(result);
    }

}
