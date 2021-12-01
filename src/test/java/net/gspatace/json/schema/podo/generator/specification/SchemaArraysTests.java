package net.gspatace.json.schema.podo.generator.specification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.gspatace.json.schema.podo.generator.specification.models.ArrayItems;
import net.gspatace.json.schema.podo.generator.specification.models.JsonSchema;
import net.gspatace.json.schema.podo.generator.specification.models.Properties;
import net.gspatace.json.schema.podo.generator.specification.models.Property;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.util.ArrayList;
import java.util.List;
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
    public void DeserializeArrayWithSimpleTypes() throws JsonProcessingException {
        final Optional<String> jsonSchema = getSchemaFromResource(this.getClass().getClassLoader(), "schemas/SchemaArrays.json");
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
    public void DeserializeArrayWithComplexTypes() throws JsonProcessingException {
        final Optional<String> jsonSchema = getSchemaFromResource(this.getClass().getClassLoader(), "schemas/SchemaArrays.json");
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
        final Property rateProperty = items.getProperties().get().getPropertyByName("rate").get();
        assertEquals(JsonDataTypes.INTEGER, rateProperty.getType());
        final Property userProperty = items.getProperties().get().getPropertyByName("user").get();
        assertEquals("User that gave this review", userProperty.getDescription());
    }

    @Test
    public void SerializeSimpleArrayItems() throws JsonProcessingException, JSONException {
        final ArrayItems arrayItems = ArrayItems.builder().type(JsonDataTypes.STRING).build();
        final ObjectMapper objectMapper = createDefaultObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        final String actual = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayItems);
        final Optional<String> expected = getSchemaFromResource(this.getClass().getClassLoader(), "schemas/SimpleArrayProperty.json");
        JSONAssert.assertEquals(expected.get(), actual, JSONCompareMode.STRICT);
    }

    @Test
    public void SerializeComplexArrayItems() throws JsonProcessingException, JSONException {
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
        final ObjectMapper objectMapper = createDefaultObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        final String actualRepresentation = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(arrayItems);
        final Optional<String> expectedRepresentation = getSchemaFromResource(this.getClass().getClassLoader(), "schemas/ComplexArrayProperty.json");
        JSONAssert.assertEquals(expectedRepresentation.get(),actualRepresentation,JSONCompareMode.STRICT);
    }
}
