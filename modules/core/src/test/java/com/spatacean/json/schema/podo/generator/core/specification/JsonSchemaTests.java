package com.spatacean.json.schema.podo.generator.core.specification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatacean.json.schema.podo.generator.core.specification.models.JsonSchema;
import com.spatacean.json.schema.podo.generator.core.specification.models.Properties;
import com.spatacean.json.schema.podo.generator.core.specification.models.Property;
import com.spatacean.json.schema.podo.generator.core.utils.ObjectMapperFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JsonSchemaTests {
    private static final String JSON_SCHEMA_AS_JSON = """
            {
              "$schema": "https://json-schema.org/draft/2020-12/schema",
              "$id": "https://example.com/product.schema.json",
              "title": "Product",
              "description": "A product from Acme's catalog",
              "type": "object",
              "properties": {
                "productId": {
                  "description": "The unique identifier for a product",
                  "type": "integer"
                },
                "productName": {
                  "description": "Name of the product",
                  "type": "string"
                }
              },
              "required": [ "productId", "productName" ]
            }""";
    private final ObjectMapper objectMapper = ObjectMapperFactory.createDefaultObjectMapper();
    private JsonSchema jsonSchemaTest;

    @BeforeEach
    void Setup() {
        final Properties properties = new Properties();
        final List<Property> propertyList = new ArrayList<>();
        propertyList.add(Property.builder().propertyName("prop1").description("Description of 1st property").type(JsonDataTypes.OBJECT).build());
        propertyList.add(Property.builder().propertyName("prop2").description("Description of 2nd property").type(JsonDataTypes.INTEGER).build());
        propertyList.add(Property.builder().propertyName("prop3").description("Description of 3nd property").type(JsonDataTypes.BOOLEAN).build());
        properties.setPropertyList(propertyList);

        final List<String> required = new ArrayList<>();
        required.add("prop1");
        required.add("prop3");

        jsonSchemaTest = new JsonSchema();
        jsonSchemaTest.setSchema("https://some-json-schema.org/schema");
        jsonSchemaTest.setId("https://some-json-schema.org/id");
        jsonSchemaTest.setTitle("Some title");
        jsonSchemaTest.setProperties(properties);
        jsonSchemaTest.setType(JsonDataTypes.OBJECT);
        jsonSchemaTest.setRequired(required);
    }

    @Test
    void simpleDeserialization() throws JsonProcessingException {
        final JsonSchema jsonSchema = objectMapper.readValue(JSON_SCHEMA_AS_JSON, JsonSchema.class);
        assert jsonSchema != null;
        assertEquals("Product", jsonSchema.getTitle());
        assertEquals("A product from Acme's catalog", jsonSchema.getDescription());
        assertEquals(JsonDataTypes.OBJECT, jsonSchema.getType());

        assertEquals(2, jsonSchema.getRequired().size());
        assertTrue(jsonSchema.getRequired().contains("productId") &&
                jsonSchema.getRequired().contains("productName"));
    }

    @Test
    void bidirectionalConversion_startFromPojo() throws JsonProcessingException {
        final String pojoRepresentation = objectMapper.writeValueAsString(jsonSchemaTest);
        final JsonSchema reconstructedSchema = objectMapper.readValue(pojoRepresentation, JsonSchema.class);
        assertEquals(jsonSchemaTest, reconstructedSchema);
    }
}
