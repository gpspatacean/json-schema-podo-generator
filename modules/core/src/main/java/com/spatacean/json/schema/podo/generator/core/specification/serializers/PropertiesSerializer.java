package com.spatacean.json.schema.podo.generator.core.specification.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.spatacean.json.schema.podo.generator.core.specification.models.Properties;
import com.spatacean.json.schema.podo.generator.core.specification.models.Property;

import java.io.IOException;

/**
 * Serializer for the "properties" object of a JSON Object,
 * as specified in the JSON Schema.
 *
 * @author George Spătăcean
 */
public class PropertiesSerializer extends JsonSerializer<Properties> {
    @Override
    public void serialize(Properties properties, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        jsonGenerator.writeStartObject();
        for (final Property property : properties.getPropertyList()) {
            final String propertyAsJsonString = objectMapper.writeValueAsString(property);
            try (JsonParser parser = objectMapper.createParser(propertyAsJsonString)) {
                JsonNode node = parser.readValueAsTree();
                final JsonNode propBody = node.findValue(property.getPropertyName());
                jsonGenerator.writeObjectField(property.getPropertyName(), propBody);
            }
        }
        jsonGenerator.writeEndObject();
    }
}
