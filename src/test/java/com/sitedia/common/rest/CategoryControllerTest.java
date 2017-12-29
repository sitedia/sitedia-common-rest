package com.sitedia.common.rest;

import org.junit.Assert;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import com.sitedia.common.rest.dto.CategoryCreationDTO;
import com.sitedia.common.rest.dto.CategoryDTO;
import com.sitedia.common.rest.dto.CategoryUpdateDTO;
import com.sitedia.common.rest.entities.CategoryEntity;
import com.sitedia.common.rest.exceptions.TechnicalException;

@Sql(executionPhase = ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:sql/reinit-h2.sql")
public class CategoryControllerTest extends AbstractCrudControllerTest<CategoryCreationDTO, CategoryDTO, CategoryUpdateDTO, CategoryEntity, Long> {

    @Override
    protected String getBaseAPI() {
        return "http://localhost:8090/api/v1.0/categories";
    }

    @Override
    protected CategoryCreationDTO getCreationDTO1() throws TechnicalException, Exception {
        return new CategoryCreationDTO("category1");
    }

    @Override
    protected CategoryUpdateDTO getUpdateDTO1() {
        return new CategoryUpdateDTO("updated");
    }

    @Override
    protected Long getCreatedId(CategoryDTO created) {
        return created.getId();
    }

    @Override
    protected Class<CategoryDTO> getReadClass() {
        return CategoryDTO.class;
    }

    @Override
    protected void checkCreated(CategoryDTO created) {
        Assert.assertEquals("category1", created.getName());
    }

    @Override
    protected void checkUpdated(CategoryDTO updated) {
        Assert.assertEquals("updated", updated.getName());
    }

}
