package com.sitedia.common.rest.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.sitedia.common.rest.dto.DefaultUserDTO;
import com.sitedia.common.rest.entity.DefaultUserEntity;

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
