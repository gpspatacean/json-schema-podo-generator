package net.gspatace.json.schema.podo.generator.specification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.models.JsonSchema;
import net.gspatace.json.schema.podo.generator.specification.models.Properties;
import net.gspatace.json.schema.podo.generator.specification.models.Property;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static net.gspatace.json.schema.podo.generator.utils.ObjectMapperFactory.createDefaultObjectMapper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonSchemaTests {
    private static final String jsonSchemaAsJson = "{\n" +
            "  \"$schema\": \"https://json-schema.org/draft/2020-12/schema\",\n" +
            "  \"$id\": \"https://example.com/product.schema.json\",\n" +
            "  \"title\": \"Product\",\n" +
            "  \"description\": \"A product from Acme's catalog\",\n" +
            "  \"type\": \"object\",\n" +
            "  \"properties\": {\n" +
            "    \"productId\": {\n" +
            "      \"description\": \"The unique identifier for a product\",\n" +
            "      \"type\": \"integer\"\n" +
            "    },\n" +
            "    \"productName\": {\n" +
            "      \"description\": \"Name of the product\",\n" +
            "      \"type\": \"string\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"required\": [ \"productId\", \"productName\" ]\n" +
            "}";
    private final ObjectMapper objectMapper = createDefaultObjectMapper();
    private JsonSchema jsonSchemaTest;

    @Before
    public void Setup() {
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
    public void simpleDeserialization() throws JsonProcessingException {
        final JsonSchema jsonSchema = objectMapper.readValue(jsonSchemaAsJson, JsonSchema.class);
        assert jsonSchema != null;
        assertEquals("Product", jsonSchema.getTitle());
        assertEquals("A product from Acme's catalog", jsonSchema.getDescription());
        assertEquals(JsonDataTypes.OBJECT, jsonSchema.getType());

        assertEquals(2, jsonSchema.getRequired().size());
        assertTrue(jsonSchema.getRequired().contains("productId") &&
                jsonSchema.getRequired().contains("productName"));
    }

    @Test
    public void bidirectionalConversion_startFromPojo() throws JsonProcessingException {
        final String pojoRepresentation = objectMapper.writeValueAsString(jsonSchemaTest);
        final JsonSchema reconstructedSchema = objectMapper.readValue(pojoRepresentation, JsonSchema.class);
        assertEquals(jsonSchemaTest, reconstructedSchema);
    }
}
