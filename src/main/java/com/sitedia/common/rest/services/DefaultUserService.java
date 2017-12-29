package com.sitedia.common.rest.services;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.sitedia.common.rest.annotations.TransactionalThrowable;
import com.sitedia.common.rest.entities.DefaultUserEntity;
import com.sitedia.common.rest.exceptions.BusinessException;
import com.sitedia.common.rest.exceptions.TechnicalException;
import com.sitedia.common.rest.mappers.DefaultUserMapper;

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
    private EntityManager entityManager;

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
    @TransactionalThrowable
    public Object getByUsername(String username) throws BusinessException, TechnicalException {
        Query query = entityManager.createNamedQuery("user.getByMail");
        query.setParameter("mail", username);
        try {
            return mapper.toDTO((DefaultUserEntity) query.getSingleResult());
        } catch (NoResultException e) {
            Logger.getLogger(getClass().getName()).log(Level.FINE, e.getMessage(), e);
            return null;
        }
    }

}
