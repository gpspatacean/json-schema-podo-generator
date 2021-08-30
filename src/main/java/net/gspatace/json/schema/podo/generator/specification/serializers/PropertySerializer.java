package net.gspatace.json.schema.podo.generator.specification.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import net.gspatace.json.schema.podo.generator.specification.models.Property;

import java.io.IOException;

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
