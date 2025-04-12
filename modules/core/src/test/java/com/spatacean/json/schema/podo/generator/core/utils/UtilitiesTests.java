package com.spatacean.json.schema.podo.generator.core.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for utility classes and methods
 *
 * @author George Spătăcean
 */
class UtilitiesTests {

    @Test
    void checkDefaultObjectMapperSettings() {
        final ObjectMapper objectMapper = ObjectMapperFactory.createDefaultObjectMapper();
        assertTrue(objectMapper.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING));
        assertTrue(objectMapper.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING));
        assertFalse(objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT));
    }
}
