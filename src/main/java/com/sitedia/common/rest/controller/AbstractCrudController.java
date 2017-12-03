package com.sitedia.common.rest.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sitedia.common.rest.dto.ResponseListDTO;
import com.sitedia.common.rest.exception.BusinessException;
import com.sitedia.common.rest.exception.TechnicalException;
import com.sitedia.common.rest.service.AbstractCrudService;
import com.sitedia.common.rest.utils.HttpUtils;

/**
 * Abstract secured CRUD endpoint for all APIs
 * 
 * @author sitedia
 * @param <C> DTO to use for creation
 * @param <R> DTO to use for read and list
 * @param <U> DTO to use for update
 * @param <E> Entity to use (necessary to specify the service to use)
 * @param <I> Primary key class of the entity
 */
public abstract class AbstractCrudController<C, R, U, E, I> {

    /**
     * Creates
     * 
     * @param creationDTO
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @PreAuthorize("this.hasCreateAccess(#creationDTO, #request)")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public R create(@RequestBody @Valid @NotNull C creationDTO, HttpServletRequest request) throws BusinessException, TechnicalException {
        return getService().create(creationDTO);
    }

    /**
     * Lists
     * 
     * @param params
     * @param res
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @PreAuthorize("this.hasListAccess(#request)")
    @RequestMapping(method = RequestMethod.GET)
    public List<R> list(HttpServletRequest request, HttpServletResponse response) throws BusinessException, TechnicalException {
        ResponseListDTO<R> list = getService().list(HttpUtils.getParams(request));
        response.setHeader("X-Total-Count", list.getCount() + "");
        return list.getList();
    }

    /**
     * Gets by id
     * 
     * @param id
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @PreAuthorize("this.hasGetAccess(#id, #request)")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public R get(@PathVariable @NotNull I id) throws BusinessException, TechnicalException {
        return getService().get(id);
    }

    /**
     * Updates
     * 
     * @param id
     * @param updateDTO
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    @PreAuthorize("this.hasUpdateAccess(#id, #updateDTO, #request)")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public R update(@PathVariable @NotNull I id, @RequestBody @Valid @NotNull U updateDTO) throws BusinessException, TechnicalException {
        return getService().update(updateDTO, id);
    }

    /**
     * Deletes
     * 
     * @param id
     * @throws BusinessException
     * @throws AccessDeniedException
     * @throws TechnicalException
     */
    @PreAuthorize("this.hasDeleteAccess(#id, #request)")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public void delete(@PathVariable @NotNull I id) throws BusinessException, TechnicalException {
        getService().delete(id);
    }

    /**
     * Checks if the user has create access
     * @param creationDTO
     * @param request
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    public abstract boolean hasCreateAccess(C creationDTO, HttpServletRequest request) throws BusinessException, TechnicalException;

    /**
     * Checks if the user has list access
     * @param request
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    public abstract boolean hasListAccess(HttpServletRequest request) throws BusinessException, TechnicalException;

    /**
     * Checks if the user has read access
     * @param id
     * @param request
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    public abstract boolean hasGetAccess(I id, HttpServletRequest request) throws BusinessException, TechnicalException;

    /**
     * Checks if the user has update access
     * @param id
     * @param updateDTO
     * @param request
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    public abstract boolean hasUpdateAccess(I id, U updateDTO, HttpServletRequest request) throws BusinessException, TechnicalException;

    /**
     * Checks if the user has delete access
     * @param id
     * @param request
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     */
    public abstract boolean hasDeleteAccess(I id, HttpServletRequest request) throws BusinessException, TechnicalException;

    /**
     * Sets the service to be used by the endpoint
     * @return
     */
    protected abstract AbstractCrudService<C, R, U, E, I> getService();

}
