package net.gspatace.json.schema.podo.generator.rest.models;

import lombok.Data;

/**
 * DTO for specific generator options
 *
 * @author George Spătăcean
 */
@Data
public class CustomOption {
    /**
     * name of a property as registered in the
     * {@link net.gspatace.json.schema.podo.generator.core.annotations.CustomProperties}
     * e.g. "-v","--version"
     */
    private String name;

    /**
     * Value to be passed to the option
     * e.g. "1.0-RELEASE"
     */
    private String value;
}
