package net.gspatace.json.schema.podo.generator.generators.handlers;

import net.gspatace.json.schema.podo.generator.generators.JsonTypePropertyHandler;
import net.gspatace.json.schema.podo.generator.generators.MemberVariableData;
import net.gspatace.json.schema.podo.generator.generators.ModelData;
import net.gspatace.json.schema.podo.generator.specification.models.Property;

import java.util.List;

/**
 * Handler for a JSON Schema property that is a basic type
 * e.g. it is neither ARRAY, nor OBJECT
 *
 * @author George Spătăcean
 */
public class SimplePropertyHandler implements JsonTypePropertyHandler {
    @Override
    public MemberVariableData handle(final Property property, final List<ModelData> allModels) {
        final MemberVariableData.MemberVariableDataBuilder memberBuilder = MemberVariableData.builder();
        memberBuilder
                .name(property.getPropertyName())
                .description(property.getDescription())
                .jsonDataTypes(property.getType())
                .isArray(false);
        return memberBuilder.build();
    }
}
