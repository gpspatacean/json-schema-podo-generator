package net.gspatace.json.schema.podo.generator.core.services;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for specific generator options representation
 * Used to see information on each type
 *
 * @author George Spătăcean
 */
@Data
@Builder
public class OptionDescription {
    /**
     * Name of the field, as specified in
     * {@link picocli.CommandLine.Option} annotation
     */
    private final String name;

    /**
     * Description of the field, as specified in
     * {@link picocli.CommandLine.Option} annotation
     */
    private final String description;

    /**
     * Default value of the field, as specified in
     * {@link picocli.CommandLine.Option} annotation
     */
    private final String defaultValue;
}
