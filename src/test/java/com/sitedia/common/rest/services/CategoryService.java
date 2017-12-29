package com.sitedia.common.rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.sitedia.common.rest.dto.CategoryCreationDTO;
import com.sitedia.common.rest.dto.CategoryDTO;
import com.sitedia.common.rest.dto.CategoryUpdateDTO;
import com.sitedia.common.rest.entities.CategoryEntity;
import com.sitedia.common.rest.mapper.CategoryMapper;
import com.sitedia.common.rest.mappers.AbstractCrudMapper;

@Service
@Lazy
public class CategoryService extends AbstractCrudService<CategoryCreationDTO, CategoryDTO, CategoryUpdateDTO, CategoryEntity, Long> {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    protected AbstractCrudMapper<CategoryCreationDTO, CategoryDTO, CategoryUpdateDTO, CategoryEntity, Long> getMapper() {
        return categoryMapper;
    }

    @Override
    protected Class<CategoryEntity> getEntityClass() {
        return CategoryEntity.class;
    }

}
