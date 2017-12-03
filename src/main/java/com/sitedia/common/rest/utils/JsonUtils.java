package com.sitedia.common.rest.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sitedia.common.rest.exception.TechnicalException;

/**
 * JSON utils
 * @author cedric
 *
 */
public final class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonUtils() {
        // Static class
    }

    /**
     * Convert a String to the requested class
     * @param data
     * @param dataClass
     * @return
     * @throws TechnicalException
     */
    public static <T> T toModel(String data, Class<T> dataClass) throws TechnicalException {
        try {
            return mapper.readValue(data, dataClass);
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
    }

    /**
     * Convert the JSON string to a JSON node
     * @param jsonString
     * @return
     * @throws TechnicalException
     */
    public static JsonNode toJsonNode(String jsonString) throws TechnicalException {
        try {
            return mapper.readTree(jsonString);
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
    }

    /**
     * Convert the object to JSON
     * @param model
     * @return
     * @throws TechnicalException
     */
    public static JsonNode fromModelToJson(Object model) throws TechnicalException {
        try {
            String jsonString = mapper.writeValueAsString(model);
            return mapper.readTree(jsonString);
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
    }

    /**
     * Convert a Java object to a JSON string
     * @param value
     * @return
     * @throws TechnicalException
     */
    public static String toString(Object value) throws TechnicalException {
        try {
            return mapper.writeValueAsString(value);
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
    }

    /**
     * Convert a json array to a list
     * @param json
     * @param responseClass
     * @return
     * @throws TechnicalException
     */
    public static <T> List<T> toList(String json) throws TechnicalException {
        try {
            TypeReference<List<T>> typeReference = new TypeReference<List<T>>() {
            };
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new TechnicalException(e);
        }
    }

    public static ObjectNode extractForm(String form) throws UnsupportedEncodingException {
        ObjectNode json = new ObjectMapper().createObjectNode();

        String[] values = form.split("&");
        for (String value : values) {
            String[] split = value.split("=");
            json.put(split[0], split.length > 1 ? URLDecoder.decode(split[1], "UTF-8") : null);
        }

        return json;
    }

}
