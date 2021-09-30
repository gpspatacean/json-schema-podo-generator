package net.gspatace.json.schema.podo.generator.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Test;

import static net.gspatace.json.schema.podo.generator.utils.ObjectMapperFactory.createDefaultObjectMapper;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for utility classes and methods
 *
 * @author George Spătăcean
 */
public class UtilitiesTests {

    @Test
    public void checkDefaultObjectMapperSettings() {
        final ObjectMapper objectMapper = createDefaultObjectMapper();
        assertTrue(objectMapper.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING));
        assertTrue(objectMapper.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING));
        assertFalse(objectMapper.isEnabled(SerializationFeature.INDENT_OUTPUT));
    }
}
