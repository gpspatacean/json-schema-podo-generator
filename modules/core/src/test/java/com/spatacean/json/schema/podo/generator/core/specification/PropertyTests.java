package com.spatacean.json.schema.podo.generator.core.specification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatacean.json.schema.podo.generator.core.specification.models.Property;
import com.spatacean.json.schema.podo.generator.core.utils.ObjectMapperFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PropertyTests {
    private static final String PROPERTY_JSON = "{\n" +
            "\t\"productId\":{\n" +
            "\t\t\"description\":\"The unique identifier for a product\",\n" +
            "\t\t\"type\":\"integer\"\n" +
            "\t}\n" +
            "}";

    private static final ObjectMapper objectMapper = ObjectMapperFactory.createDefaultObjectMapper();

    @Test
    void nonNullObjectIsReturned() throws JsonProcessingException {
        final Property property = objectMapper.readValue(PROPERTY_JSON, Property.class);
        assertNotNull(property);
    }

    @Test
    void objectToJsonString() throws JsonProcessingException {
        final Property property = Property.builder()
                .propertyName("productId")
                .description("The unique identifier for a product")
                .type(JsonDataTypes.INTEGER)
                .build();
        final String result = objectMapper.writeValueAsString(property);
        final String linearizedExpectedJson = PROPERTY_JSON.replaceAll("[\\n\\t]", "");
        assertEquals(linearizedExpectedJson, result);
    }

    @Test
    void bidirectionalTransformation() throws JsonProcessingException {
        final Property originalProperty = Property.builder()
                .propertyName("propertyWithDescription")
                .description("The description of a property").type(JsonDataTypes.OBJECT).build();
        final String jsonRepresentation = objectMapper.writeValueAsString(originalProperty);
        final Property reconstructedProperty = objectMapper.readValue(jsonRepresentation, Property.class);
        assertEquals(originalProperty, reconstructedProperty);
    }
}
