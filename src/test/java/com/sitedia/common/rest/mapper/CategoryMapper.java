package com.sitedia.common.rest.mapper;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.sitedia.common.rest.dto.CategoryCreationDTO;
import com.sitedia.common.rest.dto.CategoryDTO;
import com.sitedia.common.rest.dto.CategoryUpdateDTO;
import com.sitedia.common.rest.entities.CategoryEntity;
import com.sitedia.common.rest.mappers.AbstractCrudMapper;

/**
 * Category mapper
 * 
 * @author cedric
 *
 */
@Component
@Lazy
public class CategoryMapper extends AbstractCrudMapper<CategoryCreationDTO, CategoryDTO, CategoryUpdateDTO, CategoryEntity, Long> {

    @Override
    protected Class<CategoryCreationDTO> getCreationDTOClass() {
        return CategoryCreationDTO.class;
    }

    @Override
    protected Class<CategoryDTO> getDTOClass() {
        return CategoryDTO.class;
    }

    @Override
    protected Class<CategoryUpdateDTO> getUpdateDTOClass() {
        return CategoryUpdateDTO.class;
    }

    @Override
    protected Class<CategoryEntity> getEntityClass() {
        return CategoryEntity.class;
    }

}
