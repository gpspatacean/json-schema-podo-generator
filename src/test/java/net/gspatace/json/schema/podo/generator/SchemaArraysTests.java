package net.gspatace.json.schema.podo.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.models.ArrayItems;
import net.gspatace.json.schema.podo.generator.specification.models.JsonSchema;
import net.gspatace.json.schema.podo.generator.specification.models.Properties;
import net.gspatace.json.schema.podo.generator.specification.models.Property;
import org.junit.Test;

import java.util.Optional;

import static net.gspatace.json.schema.podo.generator.Utils.getSchemaFromResource;
import static net.gspatace.json.schema.podo.generator.utils.ObjectMapperFactory.createDefaultObjectMapper;
import static org.junit.Assert.*;

/**
 * @author George Spătăcean
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class SchemaArraysTests {
    private final ObjectMapper objectMapper = createDefaultObjectMapper();

    @Test
    public void ArrayWithSimpleTypes() throws JsonProcessingException {
        final Optional<String> jsonSchema = getSchemaFromResource(this.getClass().getClassLoader(), "SchemaArrays.json");
        if (!jsonSchema.isPresent()) {
            assertFalse("Test Failure: failed to read input file", false);
        }
        final String schema = jsonSchema.get();
        final JsonSchema workingSchema = objectMapper.readValue(schema, JsonSchema.class);
        final Properties properties = workingSchema.getProperties();
        final Optional<Property> tagsProperty = properties.getPropertyByName("tags");
        if (!tagsProperty.isPresent()) {
            assertFalse("Test Failure: failed to read \"tags\" property", false);
        }
        assertSame(JsonDataTypes.ARRAY, tagsProperty.get().getType());
        assertSame(JsonDataTypes.STRING, tagsProperty.get().getItems().get().getType());
    }

    @Test
    public void ArrayWithComplexTypes() throws JsonProcessingException {
        final Optional<String> jsonSchema = getSchemaFromResource(this.getClass().getClassLoader(), "SchemaArrays.json");
        if (!jsonSchema.isPresent()) {
            assertFalse("Test Failure: failed to read input file", false);
        }
        final String schema = jsonSchema.get();
        final JsonSchema workingSchema = objectMapper.readValue(schema, JsonSchema.class);
        final Properties properties = workingSchema.getProperties();
        final Optional<Property> reviews = properties.getPropertyByName("reviews");
        if (!reviews.isPresent()) {
            assertFalse("Test Failure: failed to read \"reviews\" property", false);
        }
        assertEquals(JsonDataTypes.ARRAY, reviews.get().getType());
        final ArrayItems items = reviews.get().getItems().get();
        assertEquals(JsonDataTypes.OBJECT, items.getType());
        final Property rateProperty = items.getProperties().getPropertyByName("rate").get();
        assertEquals(JsonDataTypes.INTEGER, rateProperty.getType());
        final Property userProperty = items.getProperties().getPropertyByName("user").get();
        assertEquals("User that gave this review", userProperty.getDescription());
    }
}
