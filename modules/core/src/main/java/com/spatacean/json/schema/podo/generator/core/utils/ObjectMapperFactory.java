package com.spatacean.json.schema.podo.generator.core.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * This class provides usable Jackson ObjectMapper objects
 *
 * @author George Spătăcean
 */
public class ObjectMapperFactory {

    private ObjectMapperFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create a new ObjectMapper, with just default settings
     *
     * @return default mapper
     */
    public static ObjectMapper createNewObjectMapper() {
        return new ObjectMapper();
    }

    /**
     * Create a new ObjectMapper, with settings for
     * JSON Schema Serialization and Deserialization
     *
     * @return the specialized mapper
     */
    public static ObjectMapper createDefaultObjectMapper() {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper;
    }
}
