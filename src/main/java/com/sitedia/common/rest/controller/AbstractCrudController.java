package com.sitedia.common.rest.controller;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sitedia.common.rest.dto.ResponseListDTO;
import com.sitedia.common.rest.exception.BusinessException;
import com.sitedia.common.rest.exception.TechnicalException;
import com.sitedia.common.rest.service.AbstractCrudService;

/**
 * Default CRUD controller
 */
public abstract class AbstractCrudController<C, R, U, E, I> {

    protected static final String ACCESS_DENIED_MESSAGE = "You don't have permission to access this resource";

    /**
     * Create
     * 
     * @param creationDTO
     * @return
     * @throws BusinessException
     * @throws TechnicalException
     * @throws AccessDeniedException
     */
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public R create(@RequestBody @Valid @NotNull C creationDTO, HttpServletRequest request)
            throws BusinessException, TechnicalException, AccessDeniedException {

        // Check access
        if (!hasCreateAccess(creationDTO)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }

        return getService().create(creationDTO);
    }

    /**
     * Update
     * 
     * @param id
     * @param updateDTO
     * @return
     * @throws BusinessException
     * @throws AccessDeniedException
     * @throws TechnicalException
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public R update(@PathVariable @NotNull I id, @RequestBody @Valid @NotNull U updateDTO)
            throws BusinessException, AccessDeniedException, TechnicalException {

        // Check access
        if (!hasUpdateAccess(id)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }

        return getService().update(updateDTO, id);
    }

    /**
     * Delete
     * 
     * @param id
     * @throws BusinessException
     * @throws AccessDeniedException
     * @throws TechnicalException
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public void delete(@PathVariable @NotNull I id) throws BusinessException, AccessDeniedException, TechnicalException {

        // Check access
        if (!hasDeleteAccess(id)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }

        getService().delete(id);
    }

    /**
     * List
     * 
     * @param params
     * @param res
     * @return
     * @throws BusinessException
     * @throws AccessDeniedException
     * @throws TechnicalException
     */
    @RequestMapping(method = RequestMethod.GET)
    public List<R> list(HttpServletRequest request, HttpServletResponse res) throws BusinessException, AccessDeniedException, TechnicalException {
        Map<String, String[]> map = request.getParameterMap();

        // Check access
        if (!hasListAccess(map)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }

        ResponseListDTO<R> list = getService().list(getParams(map));
        res.setHeader("X-Total-Count", list.getCount() + "");
        return list.getList();
    }

    protected static Map<String, Object> getParams(Map<String, String[]> map) {
        Map<String, Object> result = new HashMap<>();
        for (Entry<String, String[]> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue()[0]);
        }
        return result;
    }

    /**
     * Get
     * 
     * @param id
     * @return
     * @throws BusinessException
     * @throws AccessDeniedException
     * @throws TechnicalException
     */
    @RequestMapping(path = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public R get(@PathVariable @NotNull I id) throws BusinessException, AccessDeniedException, TechnicalException {

        // Check access
        if (!hasGetAccess(id)) {
            throw new AccessDeniedException(ACCESS_DENIED_MESSAGE);
        }

        return getService().get(id);
    }

    protected abstract AbstractCrudService<C, R, U, E, I> getService();

    protected boolean hasCreateAccess(C creationDTO) throws BusinessException, TechnicalException {
        return true;
    }

    protected boolean hasListAccess(Map<String, String[]> map) throws BusinessException, TechnicalException {
        return true;
    }

    protected boolean hasGetAccess(I id) throws BusinessException, TechnicalException {
        return true;
    }

    protected boolean hasUpdateAccess(I id) throws BusinessException, TechnicalException {
        return true;
    }

    protected boolean hasDeleteAccess(I id) throws BusinessException, TechnicalException {
        return true;
    }

}
