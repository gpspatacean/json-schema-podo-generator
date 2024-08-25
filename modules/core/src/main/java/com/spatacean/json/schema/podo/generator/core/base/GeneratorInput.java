package com.spatacean.json.schema.podo.generator.core.base;

import lombok.Builder;
import lombok.Data;

/**
 * Holder of input needed by a generator
 * Holds:
 * <ul>
 *  <li>name of the generator</li>
 *  <li>input JSON specification file</li>
 *  <li>array of specific generator properties that are passed to their specific generators</li>
 * </ul>
 *
 * @author George Spătăcean
 */
@Data
@Builder
public class GeneratorInput {
    private final String generatorName;
    private final String inputSpec;
    private final String[] generatorSpecificProperties;
}
