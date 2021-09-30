package net.gspatace.json.schema.podo.generator.generators;

import net.gspatace.json.schema.podo.generator.specification.models.JsonSchema;

import java.util.ArrayList;
import java.util.List;

public class JsonSchemaParser {

    private JsonSchemaParser() {
        throw new IllegalStateException("Utility class");
    }

    public static JsonSchemaGenData getGeneratorData(final JsonSchema jsonSchema) {

        List<PropertyGenData> properties = new ArrayList<>();
        jsonSchema.getProperties().getPropertyList().stream().forEach(property ->
                properties.add(PropertyGenData.builder()
                        .name(property.getPropertyName())
                        .jsonDataTypes(property.getType())
                        .description(property.getDescription())
                        .build()));

        return JsonSchemaGenData.builder()
                .name(jsonSchema.getTitle())
                .properties(properties)
                .build();
    }
}
