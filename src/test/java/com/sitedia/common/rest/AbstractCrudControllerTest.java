package com.sitedia.common.rest;

import org.junit.Test;

import com.sitedia.common.rest.dto.StatusDTO;
import com.sitedia.common.rest.exceptions.TechnicalException;

/**
 * Site test
 * 
 * @author cedric
 *
 */
public abstract class AbstractCrudControllerTest<C, R, U, E, I> extends AbstractTest {

    @Test
    public void testCreate() throws TechnicalException, Exception {
        String cookie = login("admin@localhost", "admin123", 200);
        R created = post(getBaseAPI(), getCreationDTO1(), getReadClass(), cookie, 200);
        checkCreated(created);

        get(getBaseAPI() + "/" + getCreatedId(created), getReadClass(), cookie, 200);
    }

    @Test
    public void testUpdate() throws TechnicalException, Exception {
        String cookie = login("admin@localhost", "admin123", 200);
        R created = post(getBaseAPI(), getCreationDTO1(), getReadClass(), cookie, 200);
        put(getBaseAPI() + "/" + getCreatedId(created), getUpdateDTO1(), cookie, 200);
        R updated = get(getBaseAPI() + "/" + getCreatedId(created), getReadClass(), cookie, 200);
        checkUpdated(updated);

        get(getBaseAPI() + "/" + getCreatedId(created), getReadClass(), cookie, 200);
    }

    @Test
    public void testUpdateNotFound() throws TechnicalException, Exception {
        String cookie = login("admin@localhost", "admin123", 200);
        put(getBaseAPI() + "/123456", getUpdateDTO1(), cookie, 403);
    }

    @Test
    public void testDelete() throws TechnicalException, Exception {
        String cookie = login("admin@localhost", "admin123", 200);
        R created = post(getBaseAPI(), getCreationDTO1(), getReadClass(), cookie, 200);
        delete(getBaseAPI() + "/" + getCreatedId(created), cookie, 200);
        get(getBaseAPI() + "/" + getCreatedId(created), StatusDTO.class, cookie, 403);
    }

    @Test
    public void testDeleteNotFound() throws TechnicalException, Exception {
        String cookie = login("admin@localhost", "admin123", 200);
        delete(getBaseAPI() + "/123456", cookie, 403);
    }

    @Test
    public void testListFullAdmin() throws TechnicalException, Exception {
        String cookie = login("admin@localhost", "admin123", 200);
        get(getBaseAPI(), String.class, cookie, 200);
    }

    @Test
    public void testListAnonymous() throws TechnicalException, Exception {
        get(getBaseAPI(), String.class, null, 401);
    }

    protected abstract String getBaseAPI();

    protected abstract C getCreationDTO1() throws TechnicalException, Exception;

    protected abstract U getUpdateDTO1();

    protected abstract I getCreatedId(R created);

    protected abstract Class<R> getReadClass();

    protected abstract void checkCreated(R created);

    protected abstract void checkUpdated(R updated);

}
