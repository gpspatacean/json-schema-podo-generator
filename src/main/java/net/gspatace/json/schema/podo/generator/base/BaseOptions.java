package net.gspatace.json.schema.podo.generator.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseOptions {
    private final String generatorName;
    private final String inputSpec;
    private final String outputDirectory;
    private final String[] generatorSpecificProperties;
}
