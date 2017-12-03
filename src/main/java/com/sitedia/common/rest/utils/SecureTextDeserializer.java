package com.sitedia.common.rest.utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

/**
 * Secure text deserializer used by Jackson on DTOs
 * @author cedric
 *
 */
public class SecureTextDeserializer extends JsonDeserializer<String> {

    /**
     * Cleans the given text
     */
    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String value = p.getText();
        return SanitizeUtils.sanitize(value);
    }

}
