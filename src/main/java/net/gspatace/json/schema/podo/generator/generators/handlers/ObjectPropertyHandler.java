package net.gspatace.json.schema.podo.generator.generators.handlers;

import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.generators.JsonTypePropertyHandler;
import net.gspatace.json.schema.podo.generator.generators.MemberVariableData;
import net.gspatace.json.schema.podo.generator.generators.ModelData;
import net.gspatace.json.schema.podo.generator.specification.models.Properties;
import net.gspatace.json.schema.podo.generator.specification.models.Property;

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
