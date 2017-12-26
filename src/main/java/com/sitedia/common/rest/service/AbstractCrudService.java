package com.sitedia.common.rest.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sitedia.common.rest.annotation.TransactionalThrowable;
import com.sitedia.common.rest.dao.DaoManager;
import com.sitedia.common.rest.dto.ResponseListDTO;
import com.sitedia.common.rest.exception.BusinessException;
import com.sitedia.common.rest.exception.TechnicalException;
import com.sitedia.common.rest.mapper.AbstractCrudMapper;

/**
 * Abstract CRUD service
 * 
 * @author sitedia
 * @param <C> DTO to use for creation
 * @param <R> DTO to use for read and list
 * @param <U> DTO to use for update
 * @param <E> Entity to use (necessary to specify the service to use)
 * @param <I> Primary key class of the entity
 */
public abstract class AbstractCrudService<C, R, U, E, I> {

    @Autowired
    protected DaoManager daoManager;

    /**
     * Creates the entity in database
     * 
     * @param creationDTO
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @TransactionalThrowable
    public R create(C creationDTO) throws BusinessException, TechnicalException {
        E entity = getMapper().fromCreationDTO(creationDTO);
        E created = daoManager.create(getEntityClass(), entity);

        // Perform custom code if necessary
        internalPostCreate(creationDTO, created);

        return getMapper().toDTO(created);
    }

    /**
     * Gets the entity from database
     * 
     * @param id
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @TransactionalThrowable
    public R get(I id) throws BusinessException, TechnicalException {
        E entity = daoManager.get(getEntityClass(), id);
        return entity != null ? getMapper().toDTO(entity) : null;
    }

    /**
     * Updates the entity in database
     * 
     * @param updateDTO
     * @param id
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @TransactionalThrowable
    public R update(U updateDTO, I id) throws BusinessException, TechnicalException {
        E update = getMapper().fromUpdateDTO(updateDTO, id);
        E updated = daoManager.update(getEntityClass(), update, id);

        // Perform custom code if necesary
        internalPostUpdate(id, updateDTO, updated);

        return getMapper().toDTO(updated);
    }

    /**
     * Deletes the entity in database
     * 
     * @param id
     * @throws BusinessException
     * @throws TechnicalException
     */
    @TransactionalThrowable
    public void delete(I id) throws BusinessException, TechnicalException {
        daoManager.delete(getEntityClass(), id);
    }

    /**
     * Lists entities in database
     * 
     * @param params
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @TransactionalThrowable
    public ResponseListDTO<R> list(Map<String, Object> params) throws BusinessException, TechnicalException {

        // Convert params before filtering
        Map<String, Object> convertedParams = getMapper().convertParams(params);

        // List objects
        List<E> list = daoManager.list(getEntityClass(), convertedParams);

        // Retrieve the total number of elements
        Long count = daoManager.count(getEntityClass(), convertedParams);

        // Return result
        List<R> result = getMapper().toDTOsList(list);
        return new ResponseListDTO<>(result, count);
    }

    /**
     * Post process for creation, inside the transaction
     * @param creationDTO
     * @param created
     * @throws BusinessException
     */
    protected void internalPostCreate(C creationDTO, E created) throws BusinessException {
    }

    /**
     * Post process for update, inside the transaction
     * @param id
     * @param updateDTO
     * @param updated
     * @throws BusinessException
     */
    protected void internalPostUpdate(I id, U updateDTO, E updated) throws BusinessException {
    }

    /**
     * Return the entity class to manage
     * @return
     */
    protected abstract Class<E> getEntityClass();

    /**
     * Returns the mapper to use
     * @return
     */
    protected abstract AbstractCrudMapper<C, R, U, E, I> getMapper();

}
