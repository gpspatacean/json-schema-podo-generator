package com.spatacean.json.schema.podo.generator.core.specification.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.spatacean.json.schema.podo.generator.core.specification.models.Property;

import java.io.IOException;

/**
 * Serializer for a "property", which is a child node of the
 * "properties" node of an Object, as specified by JSON Schema.
 *
 * @author George Spătăcean
 */
public class PropertySerializer extends JsonSerializer<Property> {
    @Override
    public void serialize(Property property, JsonGenerator jsonGenerator, SerializerProvider serializers) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName(property.getPropertyName());
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("description", property.getDescription());
        jsonGenerator.writeStringField("type", property.getType().toString());
        jsonGenerator.writeEndObject();
        jsonGenerator.writeEndObject();
    }
}
