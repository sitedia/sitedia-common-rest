package com.sitedia.common.rest.utils;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.sitedia.common.rest.exceptions.RestClientException;


/**
 * REST client
 * @author cedric
 *
 */
public class RestClient {

    private static final String INVALID_STATUS_MESSAGE = "Invalid status: %d, expected %s";

    private RestTemplate restTemplate;

    /**
     * Default constructor
     * @param base
     */
    public RestClient() {
        restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
        restTemplate.setErrorHandler(new RestErrorHandler());
    }

    /**
     * POST an object to the server API
     * @param body
     * @param responseClass
     * @param sessionCookie
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T post(String uri, Object body, Class<T> responseClass, String sessionCookie, Integer expectedStatus) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (sessionCookie != null) {
            headers.add(HttpHeaders.COOKIE, sessionCookie);
        }
        HttpEntity<Object> entity = new HttpEntity(body, headers);
        ResponseEntity<T> result = restTemplate.exchange(uri, HttpMethod.POST, entity, responseClass);
        if (expectedStatus != null && result.getStatusCodeValue() != expectedStatus) {
            String message = String.format(INVALID_STATUS_MESSAGE, result.getStatusCodeValue(), expectedStatus);
            throw new RestClientException(message);
        }
        return result.getBody();
    }

    /**
     * Launch a GET request
     * @param request
     * @param responseClass
     * @param sessionCookie
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> T get(String uri, Class<T> responseClass, String sessionCookie, Integer expectedStatus) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (sessionCookie != null) {
            headers.add(HttpHeaders.COOKIE, sessionCookie);
        }
        HttpEntity<Object> entity = new HttpEntity(null, headers);
        ResponseEntity<T> result = restTemplate.exchange(uri, HttpMethod.GET, entity, responseClass);
        if (expectedStatus != null && result.getStatusCodeValue() != expectedStatus) {
            String message = String.format(INVALID_STATUS_MESSAGE, result.getStatusCodeValue(), expectedStatus);
            throw new RestClientException(message);
        }
        return result.getBody();
    }

    /**
     * Update an object to the server API
     * @param body
     * @param id the identifier of the object to update
     * @param responseClass
     * @param sessionCookie
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void put(String uri, Object body, String sessionCookie, Integer expectedStatus) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (sessionCookie != null) {
            headers.add(HttpHeaders.COOKIE, sessionCookie);
        }
        HttpEntity<Object> entity = new HttpEntity(body, headers);

        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.PUT, entity, Object.class);
        if (expectedStatus != null && result.getStatusCodeValue() != expectedStatus) {
            String message = String.format(INVALID_STATUS_MESSAGE, result.getStatusCodeValue(), expectedStatus);
            throw new RestClientException(message);
        }
    }

    /**
     * Deletes a DTO
     * @param id
     * @param responseClass
     * @param sessionCookie
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void delete(String uri, String sessionCookie, Integer expectedStatus) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (sessionCookie != null) {
            headers.add(HttpHeaders.COOKIE, sessionCookie);
        }
        HttpEntity<Object> entity = new HttpEntity(null, headers);

        ResponseEntity<Object> result = restTemplate.exchange(uri, HttpMethod.DELETE, entity, Object.class);
        if (expectedStatus != null && result.getStatusCodeValue() != expectedStatus) {
            String message = String.format(INVALID_STATUS_MESSAGE, result.getStatusCodeValue(), expectedStatus);
            throw new RestClientException(message);
        }
    }

    /**
     * List entities
     * @param query
     * @param responseClass
     * @param sessionCookie
     * @return
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> List<T> list(String uri, String sessionCookie) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (sessionCookie != null) {
            headers.add(HttpHeaders.COOKIE, sessionCookie);
        }
        HttpEntity<Object> entity = new HttpEntity(null, headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class);
        if (result.getStatusCodeValue() != 200) {
            String message = String.format(INVALID_STATUS_MESSAGE, result.getStatusCodeValue(), 200);
            throw new RestClientException(message);
        }
        return JsonUtils.toList(result.getBody());
    }

    /**
     * Logs on the server
     * @param uri
     * @param body
     * @param expectedStatus
     * @return
     * @throws RestClientException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public String login(String uri, Object body, Integer expectedStatus) throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Object> entity = new HttpEntity(body, headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        int requiredStatus = expectedStatus != null ? expectedStatus : 200;
        if (result.getStatusCodeValue() != requiredStatus) {
            String message = String.format(INVALID_STATUS_MESSAGE, result.getStatusCodeValue(), requiredStatus);
            throw new RestClientException(message);
        }
        if (result.getHeaders().get(HttpHeaders.SET_COOKIE) == null) {
            return null;
        }
        List<String> cookies = result.getHeaders().get(HttpHeaders.SET_COOKIE);
        for (String cookie : cookies) {
            if (cookie.startsWith("SESSION=")) {
                return cookie.split(";")[0];
            }
        }
        return null;
    }

    /**
     * Uploads a file on the server
     * @param uri
     * @param fileName
     * @param sessionCookie
     * @param expectedStatus
     * @throws IOException
     */
    public String uploadFile(String uri, String path, String fileName, String sessionCookie, Integer expectedStatus) throws IOException {
        LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("file", new ClassPathResource(path));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        if (sessionCookie != null) {
            headers.add(HttpHeaders.COOKIE, sessionCookie);
        }
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
        ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.POST, entity, String.class);
        if (expectedStatus != null && result.getStatusCodeValue() != expectedStatus) {
            String message = String.format(INVALID_STATUS_MESSAGE, result.getStatusCodeValue(), expectedStatus);
            throw new RestClientException(message);
        }
        return result.getBody();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> T submitForm(String uri, Class<T> responseClass, String body, String sessionCookie, Integer expectedStatus)
            throws RestClientException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (sessionCookie != null) {
            headers.add(HttpHeaders.COOKIE, sessionCookie);
        }
        HttpEntity<Object> entity = new HttpEntity(body, headers);
        ResponseEntity<T> result = restTemplate.exchange(uri, HttpMethod.POST, entity, responseClass);
        if (expectedStatus != null && result.getStatusCodeValue() != expectedStatus) {
            String message = String.format(INVALID_STATUS_MESSAGE, result.getStatusCodeValue(), expectedStatus);
            throw new RestClientException(message);
        }
        return result.getBody();
    }

}
