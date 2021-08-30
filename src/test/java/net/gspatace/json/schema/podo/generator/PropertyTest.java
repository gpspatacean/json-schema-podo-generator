package net.gspatace.json.schema.podo.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.models.Property;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PropertyTest {
    private static final String propertyJson = "{\n" +
            "\t\"productId\":{\n" +
            "\t\t\"description\":\"The unique identifier for a product\",\n" +
            "\t\t\"type\":\"integer\"\n" +
            "\t}\n" +
            "}";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void Setup() {
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void nonNullObjectIsReturned() throws JsonProcessingException {
        final Property property = objectMapper.readValue(propertyJson, Property.class);
        assert property != null;
    }

    @Test
    public void objectToJsonString() throws JsonProcessingException {
        final Property property = Property.builder()
                .propertyName("productId")
                .description("The unique identifier for a product")
                .type(JsonDataTypes.INTEGER)
                .build();
        final String result = objectMapper.writeValueAsString(property);
        final String linearizedExpectedJson = propertyJson.replaceAll("[\\n\\t]", "");
        assertEquals(linearizedExpectedJson, result);
    }

    @Test
    public void bidirectionalTransformation() throws JsonProcessingException {
        final Property originalProperty = Property.builder()
                .propertyName("propertyWithDescription")
                .description("The description of a property").type(JsonDataTypes.OBJECT).build();
        final String jsonRepresentation = objectMapper.writeValueAsString(originalProperty);
        final Property reconstructedProperty = objectMapper.readValue(jsonRepresentation, Property.class);
        assertEquals(originalProperty, reconstructedProperty);
    }
}
