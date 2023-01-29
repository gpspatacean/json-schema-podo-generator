package net.gspatace.json.schema.podo.generator.core.services;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for generator representation.
 * Used for retrieval of information pertaining to a given generator,
 * while listing the available generators.
 * See {@link GeneratorsHandler#getAvailableGenerators()}
 *
 * @author George Spătăcean
 */
@Data
@Builder
public class GeneratorDescription {
    private final String name;
    private final String description;
}
