package com.sitedia.common.rest;

import java.util.HashMap;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.sitedia.common.rest.dao.DaoManager;
import com.sitedia.common.rest.dto.StatusDTO;
import com.sitedia.common.rest.exceptions.RestClientException;
import com.sitedia.common.rest.exceptions.TechnicalException;
import com.sitedia.common.rest.utils.RestClient;
import com.sitedia.common.rest.utils.SecurityFilter;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ApplicationTest.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/sql/truncate.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/02_init.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
public abstract class AbstractTest {

    private static final String FUZZING_TEXT_1 = "-)èçç_è)ç_=)àç687576\\@^@\\]\\^|";

    private static final String FUZZING_TEXT_2 = "{\"variable\": \"value\"}";

    protected RestClient restClient = new RestClient();

    @Autowired
    Environment environment;

    @Value("${server.port}")
    protected String serverPort;

    @Autowired
    @Qualifier("simpleCorsFilter")
    private SecurityFilter simpleCorsFilter;

    @Autowired
    protected DaoManager daoManager;

    @Before
    public void setup() {
        SecurityContextHolder.clearContext();
    }

    protected <R> R post(String uri, Object form, Class<R> responseClass, String cookie, int expectedStatus) throws TechnicalException, Exception {
        return restClient.post(uri, form, responseClass, cookie, expectedStatus);
    }

    protected void put(String uri, Object form, String cookie, int expectedStatus) throws TechnicalException, Exception {
        restClient.put(uri, form, cookie, expectedStatus);
    }

    protected <R> R get(String uri, Class<R> responseClass, String cookie, int expectedStatus) throws TechnicalException, Exception {
        return restClient.get(uri, responseClass, cookie, expectedStatus);
    }

    protected <R> Object list(String uri, Class<R> responseClass, String cookie) throws TechnicalException, Exception {
        return restClient.list(uri, cookie);
    }

    protected void delete(String uri, String cookie, int expectedStatus) throws TechnicalException, Exception {
        restClient.delete(uri, cookie, expectedStatus);
    }

    protected String upload(String uri, String path, String fileName, String cookie, int expectedStatus) throws TechnicalException, Exception {
        return restClient.uploadFile(uri, path, fileName, cookie, expectedStatus);
    }

    protected <R> R submitForm(String uri, Class<R> responseClass, String body, String cookie, int expectedStatus) throws RestClientException {
        return restClient.submitForm(uri, responseClass, body, cookie, expectedStatus);
    }

    protected void postFuzzing(String uri) throws TechnicalException, Exception {
        restClient.post(uri, FUZZING_TEXT_1, StatusDTO.class, null, 403);
        restClient.post(uri, FUZZING_TEXT_2, StatusDTO.class, null, 403);
    }

    protected String login(String username, String password, Integer expectedStatus) throws Exception {
        String form = "username=" + username + "&password=" + password;
        return restClient.login("http://localhost:" + serverPort + "//login.html", form, expectedStatus);
    }

    protected <T> int countInDatabase(Class<T> entityClass) {
        return daoManager.count(entityClass, new HashMap<>()).intValue();
    }

}