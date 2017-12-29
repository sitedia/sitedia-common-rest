package com.sitedia.common.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sitedia.common.rest.dto.CategoryCreationDTO;
import com.sitedia.common.rest.dto.CategoryDTO;
import com.sitedia.common.rest.dto.CategoryUpdateDTO;
import com.sitedia.common.rest.entities.CategoryEntity;
import com.sitedia.common.rest.exceptions.BusinessException;
import com.sitedia.common.rest.exceptions.TechnicalException;
import com.sitedia.common.rest.services.AbstractCrudService;
import com.sitedia.common.rest.services.CategoryService;
import com.sitedia.common.rest.services.DefaultUserService;

/**
 * Category controller
 * 
 * @author cedric
 *
 */
@RestController
@RequestMapping(path = { "/api/v1.0/categories" }, produces = "application/json;charset=UTF-8")
public class CategoryController extends AbstractCrudController<CategoryCreationDTO, CategoryDTO, CategoryUpdateDTO, CategoryEntity, Long> {

    @Lazy
    @Autowired
    private CategoryService categoryService;

    @Lazy
    @Autowired
    private DefaultUserService defaultUserService;

    @Override
    public boolean hasCreateAccess(CategoryCreationDTO creationDTO, Authentication authentication) throws BusinessException, TechnicalException {
        return isAdmin(authentication);
    }

    @Override
    public boolean hasListAccess(Authentication authentication) throws BusinessException, TechnicalException {
        return isAdmin(authentication);
    }

    @Override
    public boolean hasGetAccess(Long id, Authentication authentication) throws BusinessException, TechnicalException {
        return isAdmin(authentication);
    }

    @Override
    public boolean hasUpdateAccess(Long id, CategoryUpdateDTO updateDTO, Authentication authentication) throws BusinessException, TechnicalException {
        return isAdmin(authentication);
    }

    @Override
    public boolean hasDeleteAccess(Long id, Authentication authentication) throws BusinessException, TechnicalException {
        return isAdmin(authentication);
    }

    @Override
    protected AbstractCrudService<CategoryCreationDTO, CategoryDTO, CategoryUpdateDTO, CategoryEntity, Long> getService() {
        return categoryService;
    }

    public boolean isAdmin(Authentication authentication) throws BusinessException, TechnicalException {
        return authentication != null && authentication.getName() != null && authentication.getName().equals("admin@localhost");
    }
}
