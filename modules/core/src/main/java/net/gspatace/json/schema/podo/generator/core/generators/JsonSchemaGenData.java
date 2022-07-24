package net.gspatace.json.schema.podo.generator.core.generators;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * List of all the models inferred from JSON Schema.
     * Each of the model will be used as context in template execution
     */
    private final List<ModelData> models;

    /**
     * List of additional generic properties that can be
     * used by each concrete generator implementation
     */
    @Builder.Default
    private Map<String,Object> additionalProperties = new HashMap<>();
}
