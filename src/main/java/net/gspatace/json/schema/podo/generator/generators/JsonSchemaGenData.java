package net.gspatace.json.schema.podo.generator.generators;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class JsonSchemaGenData {
    private final String name;
    private final List<PropertyGenData> properties;
}
