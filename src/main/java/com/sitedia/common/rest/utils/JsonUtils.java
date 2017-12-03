package com.sitedia.common.rest.utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sitedia.common.rest.exception.TechnicalException;

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

}
