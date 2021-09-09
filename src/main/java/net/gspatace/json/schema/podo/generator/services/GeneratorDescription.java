package net.gspatace.json.schema.podo.generator.services;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneratorDescription {
    private final String name;
    private final String description;
}
