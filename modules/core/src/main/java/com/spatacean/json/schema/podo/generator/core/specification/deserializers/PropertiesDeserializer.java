package com.spatacean.json.schema.podo.generator.core.specification.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatacean.json.schema.podo.generator.core.specification.models.Properties;
import com.spatacean.json.schema.podo.generator.core.specification.models.Property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Deserializer for a "property", which is a child node of the
 * "properties" node of JSON Object, as specified by JSON Schema
 *
 * @author George Spătăcean
 */
public class PropertiesDeserializer extends JsonDeserializer<Properties> {
    @Override
    public Properties deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final Properties properties = new Properties();
        final List<Property> propertyList = new ArrayList<>();
        final JsonNode jsonNode = jsonParser.readValueAsTree();
        final Iterator<Map.Entry<String, JsonNode>> fields = jsonNode.fields();
        final ObjectMapper objectMapper = new ObjectMapper();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> property = fields.next();
            final String propertyName = property.getKey();
            final String propertyBody = property.getValue().toString();
            final StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("{\"").append(propertyName).append("\":") // property name as object field
                    .append(propertyBody)
                    .append("}");
            final Property prop = objectMapper.readValue(stringBuilder.toString(), Property.class);
            propertyList.add(prop);
        }
        properties.setPropertyList(propertyList);
        return properties;
    }
}
