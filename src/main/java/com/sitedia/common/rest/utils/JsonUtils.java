package com.sitedia.common.rest.utils;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitedia.common.rest.exceptions.TechnicalException;

/**
 * JSON utils
 * @author cedric
 *
 */
public final class JsonUtils {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Private constructor
     */
    private JsonUtils() {
        // Static class
    }

    /**
     * Converts a Java object to a JSON string
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
     * Convert the JSON string to a JSON node
     * @param jsonString
     * @return
     * @throws TechnicalException
     */
    public static JsonNode toJsonNode(Object value) throws TechnicalException {
        return mapper.valueToTree(value);
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

}
