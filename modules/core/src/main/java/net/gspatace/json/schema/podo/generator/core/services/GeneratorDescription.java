package net.gspatace.json.schema.podo.generator.core.services;

/**
 * DTO for generator representation.
 * Used for retrieval of information pertaining to a given generator,
 * while listing the available generators.
 * See {@link GeneratorsHandler#getAvailableGenerators()}
 *
 * @param name of the generator
 * @param description of the generator
 *
 * @author George Spătăcean
 */
public record GeneratorDescription(String name, String description) {
}
