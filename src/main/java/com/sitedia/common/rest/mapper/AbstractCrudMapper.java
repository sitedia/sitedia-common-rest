package com.sitedia.common.rest.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.sitedia.common.rest.dao.DaoManager;
import com.sitedia.common.rest.exception.BusinessException;
import com.sitedia.common.rest.exception.TechnicalException;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

/**
 * Abstract CRUD mapper between DTOs and entities. Based on Orika.
 * 
 * @author sitedia
 *
 * @param <C> DTO to use for creation
 * @param <R> DTO to use for read and list
 * @param <U> DTO to use for update
 * @param <E> Entity to use (necessary to specify the service to use)
 * @param <I> Primary key class of the entity
 */
public abstract class AbstractCrudMapper<C, R, U, E, I> {

    protected MapperFactory mapperFactory;

    @Autowired
    protected DaoManager daoManager;

    /**
     * Default constructor
     */
    public AbstractCrudMapper() {
        mapperFactory = new DefaultMapperFactory.Builder().build();

        // Entity -> DTO
        mapperFactory.classMap(getEntityClass(), getDTOClass()).mapNulls(false).byDefault().register();

        // CreationDTO -> Entity
        mapperFactory.classMap(getCreationDTOClass(), getEntityClass()).mapNulls(false).byDefault().register();

        // UpdateDTO -> Entity
        mapperFactory.classMap(getUpdateDTOClass(), getEntityClass()).mapNulls(false).byDefault().register();
    }

    /**
     * Converts the entity to a DTO
     * 
     * @param entity
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    public R toDTO(E entity) throws BusinessException, TechnicalException {
        return mapperFactory.getMapperFacade().map(entity, getDTOClass());
    }

    /**
     * Returns the entity for creation
     * 
     * @param creationDTO
     * @param author
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    public E fromCreationDTO(C creationDTO) throws BusinessException, TechnicalException {
        return mapperFactory.getMapperFacade().map(creationDTO, getEntityClass());

    }

    /**
     * Returns the entity for update
     * 
     * @param updateDTO
     * @param id
     * @param author
     * @return
     * @throws BusinessException
     */
    public E fromUpdateDTO(U updateDTO, I id) throws BusinessException, TechnicalException {
        E entity = daoManager.get(getEntityClass(), id);
        mapperFactory.getMapperFacade().map(updateDTO, entity);
        return entity;
    }

    /**
     * Convert a list of entities to DTOs
     * 
     * @param entities
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    public List<R> toDTOsList(List<E> entities) throws BusinessException, TechnicalException {
        List<R> result = new ArrayList<>();
        for (E entity : entities) {
            result.add(toDTO(entity));
        }
        return result;
    }

    /**
     * Apply conversions on the request parameters: Class changes, n-n relations
     * management, ...
     * @param params
     * @return
     */
    public Map<String, Object> convertParams(Map<String, Object> params) {
        return params;
    }

    /**
     * Return the DTO class for creation
     * @return
     */
    protected abstract Class<C> getCreationDTOClass();

    /**
     * Return the DTO class for read and list
     * @return
     */
    protected abstract Class<R> getDTOClass();

    /**
     * Return the DTO class for update
     * @return
     */
    protected abstract Class<U> getUpdateDTOClass();

    /**
     * Return the entity class for mapping
     * @return
     */
    protected abstract Class<E> getEntityClass();

}
