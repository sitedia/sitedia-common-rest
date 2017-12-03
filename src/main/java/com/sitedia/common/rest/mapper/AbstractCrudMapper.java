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
 * DTO / Entity CRUD mapper
 * 
 * @author cedric
 *
 * @param <C>
 * @param <R>
 * @param <U>
 * @param <E>
 * @param <I>
 */
public abstract class AbstractCrudMapper<C, R, U, E, I> {

    @Autowired
    protected DaoManager daoManager;

    protected MapperFactory mapperFactory;

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

    public Map<String, Object> convertParams(Map<String, Object> params) {
        return params;
    }

    protected abstract Class<C> getCreationDTOClass();

    protected abstract Class<R> getDTOClass();

    protected abstract Class<U> getUpdateDTOClass();

    protected abstract Class<E> getEntityClass();

}
