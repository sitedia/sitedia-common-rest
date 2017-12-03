package com.sitedia.common.rest.service;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;

import com.sitedia.common.rest.dao.DAOManager;
import com.sitedia.common.rest.dto.ResponseListDTO;
import com.sitedia.common.rest.exception.BusinessException;
import com.sitedia.common.rest.exception.TechnicalException;
import com.sitedia.common.rest.mapper.AbstractCrudMapper;
import com.sitedia.common.rest.utils.JsonUtils;

/**
 * CRUD service
 * 
 * @author cedric
 *
 * @param <C>
 * @param <R>
 * @param <U>
 * @param <E>
 * @param <I>
 */
public abstract class AbstractCrudService<C, R, U, E, I> {

    protected Logger logger = Logger.getLogger(getClass().getName());

    @Autowired
    protected DAOManager daoManager;

    /**
     * Create the entity in database
     * 
     * @param creationDTO
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @Transactional
    public R create(C creationDTO) throws BusinessException, TechnicalException {
        E entity = getMapper().fromCreationDTO(creationDTO);
        E created = daoManager.create(getEntityClass(), entity, null, null);

        // Perform custom code if necesary
        internalPostCreate(creationDTO, created);

        logger.info(String.format("%s created: %s", getEntityClass(), JsonUtils.toString(creationDTO)));
        return getMapper().toDTO(created);
    }

    /**
     * gets the entity
     * 
     * @param id
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @Transactional
    public R get(I id) throws BusinessException, TechnicalException {
        E entity = daoManager.get(getEntityClass(), id);

        // Check that the page exists
        if (entity == null) {
            throw new BusinessException("Page not found");
        }

        return getMapper().toDTO(entity);
    }

    /**
     * Update
     * 
     * @param updateDTO
     * @param id
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @Transactional
    public R update(U updateDTO, I id) throws BusinessException, TechnicalException {
        E update = getMapper().fromUpdateDTO(updateDTO, id);
        E updated = daoManager.update(getEntityClass(), update, id);

        // Perform custom code if necesary
        internalPostUpdate(id, updateDTO, updated);

        logger.info(String.format("%s udpated: %s", getEntityClass(), JsonUtils.toString(updated)));
        return getMapper().toDTO(updated);
    }

    /**
     * Delete
     * 
     * @param id
     * @throws BusinessException
     * @throws TechnicalException
     */
    @Transactional
    public void delete(I id) throws BusinessException, TechnicalException {
        logger.info(String.format("%s deleted: %s", getEntityClass(), JsonUtils.toString(id)));
        daoManager.delete(getEntityClass(), id);
    }

    /**
     * List
     * 
     * @param params
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @Transactional
    public ResponseListDTO<R> list(Map<String, Object> params) throws BusinessException, TechnicalException {
        Map<String, Object> convertedParams = getMapper().convertParams(params);
        List<E> list = daoManager.list(getEntityClass(), convertedParams);
        Long count = daoManager.count(getEntityClass(), convertedParams);
        List<R> result = getMapper().toDTOsList(list);
        return new ResponseListDTO<>(result, count);
    }

    protected void internalPostCreate(C creationDTO, E created) throws BusinessException {
    }

    protected void internalPostUpdate(I id, U updateDTO, E updated) throws BusinessException {
    }

    protected abstract Class<E> getEntityClass();

    protected abstract AbstractCrudMapper<C, R, U, E, I> getMapper();

}
