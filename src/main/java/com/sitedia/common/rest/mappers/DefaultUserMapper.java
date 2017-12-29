package com.sitedia.common.rest.mappers;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.sitedia.common.rest.dto.DefaultUserDTO;
import com.sitedia.common.rest.entities.DefaultUserEntity;
import com.sitedia.common.rest.exceptions.BusinessException;
import com.sitedia.common.rest.exceptions.TechnicalException;

/**
 * User mapper
 * 
 * @author cedric
 *
 */
@Lazy
@Component
public class DefaultUserMapper extends AbstractCrudMapper<Object, DefaultUserDTO, Object, DefaultUserEntity, String> {

    @Override
    public DefaultUserDTO toDTO(DefaultUserEntity entity) throws BusinessException, TechnicalException {
        DefaultUserDTO result = new DefaultUserDTO();
        result.setUsername(entity.getMail());
        return result;
    }

    @Override
    protected Class<Object> getCreationDTOClass() {
        return Object.class;
    }

    @Override
    protected Class<DefaultUserDTO> getDTOClass() {
        return DefaultUserDTO.class;
    }

    @Override
    protected Class<Object> getUpdateDTOClass() {
        return Object.class;
    }

    @Override
    protected Class<DefaultUserEntity> getEntityClass() {
        return DefaultUserEntity.class;
    }

}
