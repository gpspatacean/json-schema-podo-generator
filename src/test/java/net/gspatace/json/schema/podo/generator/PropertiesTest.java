package net.gspatace.json.schema.podo.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.models.Properties;
import net.gspatace.json.schema.podo.generator.specification.models.Property;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class PropertiesTest {
    private Properties properties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void Setup() {
        properties = new Properties();
        final List<Property> propertyList = new ArrayList<>();
        propertyList.add(Property.builder().propertyName("prop1").description("Description of 1st property").type(JsonDataTypes.OBJECT).build());
        propertyList.add(Property.builder().propertyName("prop2").description("Description of 2nd property").type(JsonDataTypes.INTEGER).build());
        propertyList.add(Property.builder().propertyName("prop3").description("Description of 3nd property").type(JsonDataTypes.BOOLEAN).build());
        properties.setPropertyList(propertyList);

        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
    }

    @Test
    public void testFullSerialization() throws JsonProcessingException {
        final String actual = objectMapper.writeValueAsString(properties);
        final String liniarExpected = propertiesJson.replaceAll("[\\r\\n\\t]","");
        assertEquals(liniarExpected, actual);
    }

    @Test
    public void bidirectionalTransformation() throws JsonProcessingException {
        final String propertiesAsString = objectMapper.writeValueAsString(properties);
        final Properties reconvertedProperties = objectMapper.readValue(propertiesAsString, Properties.class);
        assertEquals(properties,reconvertedProperties);
    }

    private static final String propertiesJson = "{\n" +
            //"\t\"properties\":{\n" +
            "\t\t\"prop1\":{\n" +
            "\t\t\t\"description\":\"Description of 1st property\",\n" +
            "\t\t\t\"type\":\"object\"\n" +
            "\t\t},\n" +
            "\t\t\"prop2\":{\n" +
            "\t\t\t\"description\":\"Description of 2nd property\",\n" +
            "\t\t\t\"type\":\"integer\"\n" +
            "\t\t},\n" +
            "\t\t\"prop3\":{\n" +
            "\t\t\t\"description\":\"Description of 3nd property\",\n" +
            "\t\t\t\"type\":\"boolean\"\n" +
            "\t\t}\n" +
            "\t}\n";// +
            //"}";
}
