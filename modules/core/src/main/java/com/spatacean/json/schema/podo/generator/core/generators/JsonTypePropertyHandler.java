package com.spatacean.json.schema.podo.generator.core.generators;

import com.spatacean.json.schema.podo.generator.core.specification.models.Property;

import java.util.List;

/**
 * Handler interface for types of JSON Schema properties
 *
 * @author George Spătăcean
 */
public interface JsonTypePropertyHandler {
    /**
     * Build a {@link MemberVariableData}, starting from a JSON Schema property.
     * Optionally, depending on the type of the property, also build a {@link ModelData}
     * @param property target JSON Schema property
     * @param allModels collection of all models, to which a new {@link ModelData}
     *                  can be appended.
     * @return representation of a member variable
     */
    MemberVariableData handle(final Property property, final List<ModelData> allModels);
}
