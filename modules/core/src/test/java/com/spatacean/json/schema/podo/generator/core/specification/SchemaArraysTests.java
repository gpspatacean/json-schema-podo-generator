package com.spatacean.json.schema.podo.generator.core.specification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.spatacean.json.schema.podo.generator.core.Utils;
import com.spatacean.json.schema.podo.generator.core.utils.ObjectMapperFactory;
import com.spatacean.json.schema.podo.generator.core.specification.models.ArrayItems;
import com.spatacean.json.schema.podo.generator.core.specification.models.JsonSchema;
import com.spatacean.json.schema.podo.generator.core.specification.models.Properties;
import com.spatacean.json.schema.podo.generator.core.specification.models.Property;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author George Spătăcean
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
class SchemaArraysTests {
    private final ObjectMapper defaultObjectMapper = ObjectMapperFactory.createDefaultObjectMapper();

    @Test
    void DeserializeArrayWithSimpleTypes() throws JsonProcessingException {
        final Optional<String> jsonSchema = Utils.getSchemaFromResource(this.getClass().getClassLoader(), "schemas/SchemaArrays.json");
        assertTrue(jsonSchema.isPresent(), "Test Failure: failed to read input file");
        final String schema = jsonSchema.get();
        final JsonSchema workingSchema = defaultObjectMapper.readValue(schema, JsonSchema.class);
        final Properties properties = workingSchema.getProperties();
        final Optional<Property> tagsProperty = properties.getPropertyByName("tags");
        assertTrue(tagsProperty.isPresent(), "Test Failure: failed to read \"tags\" property");
        assertSame(JsonDataTypes.ARRAY, tagsProperty.get().getType());
        assertSame(JsonDataTypes.STRING, tagsProperty.get().getItems().get().getType());
    }

    @Test
    void DeserializeArrayWithComplexTypes() throws JsonProcessingException {
        final Optional<String> jsonSchema = Utils.getSchemaFromResource(this.getClass().getClassLoader(), "schemas/SchemaArrays.json");
        assertTrue(jsonSchema.isPresent(), "Test Failure: failed to read input file");
        final String schema = jsonSchema.get();
        final JsonSchema workingSchema = defaultObjectMapper.readValue(schema, JsonSchema.class);
        final Properties properties = workingSchema.getProperties();
        final Optional<Property> reviews = properties.getPropertyByName("reviews");
        assertTrue(reviews.isPresent(), "Test Failure: failed to read \"reviews\" property");
        assertEquals(JsonDataTypes.ARRAY, reviews.get().getType());
        final ArrayItems items = reviews.get().getItems().get();
        assertEquals(JsonDataTypes.OBJECT, items.getType());
        final Property rateProperty = items.getProperties().get().getPropertyByName("rate").get();
        assertEquals(JsonDataTypes.INTEGER, rateProperty.getType());
        final Property userProperty = items.getProperties().get().getPropertyByName("user").get();
        assertEquals("User that gave this review", userProperty.getDescription());
    }

    @Test
    void SerializeSimpleArrayItems() throws JsonProcessingException, JSONException {
        final ArrayItems arrayItems = ArrayItems.builder().type(JsonDataTypes.STRING).build();
        final ObjectMapper objectMapper = ObjectMapperFactory.createDefaultObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        final String actual = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayItems);
        final Optional<String> expected = Utils.getSchemaFromResource(this.getClass().getClassLoader(), "schemas/SimpleArrayProperty.json");
        JSONAssert.assertEquals(expected.get(), actual, JSONCompareMode.STRICT);
    }

    @Test
    void SerializeComplexArrayItems() throws JsonProcessingException, JSONException {
        final Properties arrayProperties = new Properties();
        final List<Property> propertyList = new ArrayList<>();
        propertyList.add(Property.builder()
                .propertyName("rate")
                .type(JsonDataTypes.INTEGER)
                .description("Rating from 1 to 10")
                .build());
        propertyList.add(Property.builder()
                .propertyName("user")
                .type(JsonDataTypes.STRING)
                .description("User that gave this review")
                .build());

        arrayProperties.setPropertyList(propertyList);
        final ArrayItems.ArrayItemsBuilder arrayItemsBuilder = ArrayItems.builder();
        arrayItemsBuilder
                .type(JsonDataTypes.OBJECT)
                .properties(Optional.of(arrayProperties));
        final ArrayItems arrayItems = arrayItemsBuilder.build();
        final ObjectMapper objectMapper = ObjectMapperFactory.createDefaultObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        final String actualRepresentation = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayItems);
        final Optional<String> expectedRepresentation = Utils.getSchemaFromResource(this.getClass().getClassLoader(), "schemas/ComplexArrayProperty.json");
        JSONAssert.assertEquals(expectedRepresentation.get(), actualRepresentation, JSONCompareMode.STRICT);
    }
}
