package com.spatacean.json.schema.podo.generator.core.generators.handlers;

import com.spatacean.json.schema.podo.generator.core.generators.JsonTypePropertyHandler;
import com.spatacean.json.schema.podo.generator.core.generators.MemberVariableData;
import com.spatacean.json.schema.podo.generator.core.generators.ModelData;
import com.spatacean.json.schema.podo.generator.core.specification.models.Properties;
import com.spatacean.json.schema.podo.generator.core.specification.models.Property;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author George Spătăcean
 * <p>
 * Handler for Object Type JSON Schema Objects.
 */
@Slf4j
public class ObjectPropertyHandler implements JsonTypePropertyHandler {
    @Override
    public MemberVariableData handle(Property property, final List<ModelData> allModels) {
        final ModelData newModel = objectPropertyToModel(property, allModels);
        final MemberVariableData.MemberVariableDataBuilder memberBuilder = MemberVariableData.builder();
        memberBuilder
                .name(property.getPropertyName())
                .description(property.getDescription())
                .jsonDataTypes(property.getType())
                .isArray(false)
                .innerModel(Optional.of(newModel));
        allModels.add(newModel);
        return memberBuilder.build();
    }

    private ModelData objectPropertyToModel(final Property property, List<ModelData> allModels) {
        final Optional<Properties> properties = property.getProperties();
        if (!properties.isPresent()) {
            log.error("No properties on Object Type parent property");
            return ModelData.builder().build();
        } else {
            final List<Property> propertyList = properties.get().getPropertyList();
            final List<MemberVariableData> members = new ArrayList<>();

            propertyList.stream().forEach(prop -> {
                final JsonTypePropertyHandler propertyHandler = PropertyHandlerFactory.getForProperty(prop);
                final MemberVariableData memberVariableData = propertyHandler.handle(prop, allModels);
                members.add(memberVariableData);
            });

            final ModelData.ModelDataBuilder modelDataBuilder = ModelData.builder();
            modelDataBuilder
                    .modelName(property.getPropertyName())
                    .members(members);

            return modelDataBuilder.build();
        }
    }
}
