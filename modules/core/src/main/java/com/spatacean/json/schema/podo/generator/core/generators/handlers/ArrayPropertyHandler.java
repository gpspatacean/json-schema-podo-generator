package com.spatacean.json.schema.podo.generator.core.generators.handlers;

import com.spatacean.json.schema.podo.generator.core.generators.JsonTypePropertyHandler;
import com.spatacean.json.schema.podo.generator.core.generators.MemberVariableData;
import com.spatacean.json.schema.podo.generator.core.generators.ModelData;
import com.spatacean.json.schema.podo.generator.core.specification.JsonDataTypes;
import com.spatacean.json.schema.podo.generator.core.specification.models.ArrayItems;
import com.spatacean.json.schema.podo.generator.core.specification.models.Properties;
import com.spatacean.json.schema.podo.generator.core.specification.models.Property;
import lombok.extern.slf4j.Slf4j;
import org.jibx.schema.codegen.extend.DefaultNameConverter;
import org.jibx.schema.codegen.extend.NameConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author George Spătăcean
 * <p>
 * Handler for transformation of Array Type JSON Schema Objects
 */
@Slf4j
public class ArrayPropertyHandler implements JsonTypePropertyHandler {
    @Override
    public MemberVariableData handle(final Property property, List<ModelData> allModels) {
        if (property.getItems().isPresent()) {
            final ArrayItems arrayItems = property.getItems().get();
            if (arrayItems.getType() == JsonDataTypes.OBJECT) {
                final ModelData newModel = getModelForComplexArrayType(property, allModels);
                final MemberVariableData.MemberVariableDataBuilder memberVariableDataBuilder = MemberVariableData.builder();
                memberVariableDataBuilder
                        .name(property.getPropertyName())
                        .description(property.getDescription())
                        .jsonDataTypes(property.getType())
                        .isArray(true)
                        .innerModel(Optional.of(newModel))
                        .dataType(newModel.getModelName());
                allModels.add(newModel);
                return memberVariableDataBuilder.build();
            } else {
                return getMemberFromSimpleArrayType(property);
            }
        } else {
            throw new IllegalStateException("Array type property has no items specified");
        }
    }

    private MemberVariableData getMemberFromSimpleArrayType(final Property property) {
        final MemberVariableData.MemberVariableDataBuilder memberBuilder = MemberVariableData.builder();
        property.getItems().ifPresent(arrayItems -> {
            final String arrayDataType = arrayItems.getType().toString();
            memberBuilder
                    .name(property.getPropertyName())
                    .description(property.getDescription())
                    .jsonDataTypes(property.getType())
                    .isArray(true)
                    .dataType(arrayDataType);
        });
        return memberBuilder.build();
    }

    private ModelData getModelForComplexArrayType(final Property property, List<ModelData> allModels) {
        final Optional<ArrayItems> arrayItems = property.getItems();
        if (!arrayItems.isPresent()) {
            log.warn("No array items definition found");
            return ModelData.builder().build();
        } else {
            final ArrayItems items = arrayItems.get();
            if (JsonDataTypes.OBJECT != items.getType()) {
                log.error("Currently Array can only have Objects or Simple Data types");
                return ModelData.builder().build();
            }

            final Optional<Properties> properties = items.getProperties();
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
                final NameConverter nameConverter = new DefaultNameConverter();
                final String modelName = nameConverter.depluralize(property.getPropertyName());
                modelDataBuilder
                        .modelName(modelName)
                        .members(members);

                return modelDataBuilder.build();
            }
        }
    }
}
