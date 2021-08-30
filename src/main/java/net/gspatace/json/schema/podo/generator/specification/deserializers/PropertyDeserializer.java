package net.gspatace.json.schema.podo.generator.specification.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.models.Property;

import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

public class PropertyDeserializer extends JsonDeserializer<Property> {
    @Override
    public Property deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final JsonNode node = jsonParser.readValueAsTree();
        if (node.isObject()) {
            final Iterator<Map.Entry<String, JsonNode>> fieldIterator = node.fields();
            if (fieldIterator.hasNext()) {
                Map.Entry<String, JsonNode> property = fieldIterator.next();

                //if at this point there is another element, throw, this is bad
                if (fieldIterator.hasNext()) {
                    throw new IOException("Yes, Rico, Ka-Boom!");
                }
                final String propertyName = property.getKey();
                final JsonNode propBody = property.getValue();
                final String propDesc = propBody.get("description").asText();
                final String propType = propBody.get("type").asText();

                return Property.builder()
                        .propertyName(propertyName)
                        .description(propDesc)
                        .type(JsonDataTypes.valueOf(propType.toUpperCase(Locale.ROOT)))
                        .build();
            }
        }
        return Property.builder().build();
    }
}
