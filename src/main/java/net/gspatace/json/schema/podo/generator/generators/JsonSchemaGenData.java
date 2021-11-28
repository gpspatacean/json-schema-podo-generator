package net.gspatace.json.schema.podo.generator.generators;

import lombok.*;

import java.util.List;

/**
 * Holder of information pertaining to the JSON Schema provided as input.
 * Holds information such as main schema name, list of models.
 *
 * @author George Spătăcean
 */
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JsonSchemaGenData {
    private final String name;
    private final List<ModelData> models;
}
