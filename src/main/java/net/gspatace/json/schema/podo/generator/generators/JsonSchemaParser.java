package net.gspatace.json.schema.podo.generator.generators;

import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.models.ArrayItems;
import net.gspatace.json.schema.podo.generator.specification.models.JsonSchema;
import net.gspatace.json.schema.podo.generator.specification.models.Properties;
import net.gspatace.json.schema.podo.generator.specification.models.Property;
import org.apache.commons.lang3.NotImplementedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Utility class used for translation of JSON Schema Objects to Generator Objects,
 * that will be fed as context to the templating engine.
 *
 * @author George Spătăcean
 */
@Slf4j
public class JsonSchemaParser {

    private JsonSchemaParser() {
        throw new IllegalStateException("Utility class");
    }

    public static ModelData objectPropertyToModel(Property property) {
        if (property.getType() != JsonDataTypes.OBJECT) {
            log.error("Called for not-an-object");
            return ModelData.builder().build();
        }

        final Optional<Properties> properties = property.getProperties();
        if (!properties.isPresent()) {
            log.error("No properties on Object Type parent property");
            return ModelData.builder().build();
        } else {

            final List<Property> propertyList = properties.get().getPropertyList();
            final List<MemberVariableData> members = new ArrayList<>();

            propertyList.stream().forEach(prop -> {
                final MemberVariableData memberVariableData = simplePropertyToMember(prop);
                members.add(memberVariableData);
            });
            final ModelData.ModelDataBuilder modelDataBuilder = ModelData.builder();
            modelDataBuilder
                    .modelName(property.getPropertyName())
                    .members(members);

            return modelDataBuilder.build();
        }
    }

    public static MemberVariableData simplePropertyToMember(final Property property) {
        final MemberVariableData.MemberVariableDataBuilder memberBuilder = MemberVariableData.builder();
        memberBuilder
                .name(property.getPropertyName())
                .description(property.getDescription())
                .jsonDataTypes(property.getType())
                .isArray(false);
        return memberBuilder.build();
    }

    public static boolean isSimpleType(final JsonDataTypes type) {
        switch(type) {
            case OBJECT:
            case ARRAY:
            case STRING:
                return false;
            case INTEGER:
            case NUMBER:
            case BOOLEAN:
                return true;
            case NULL:
            case NOT_SET:
                log.warn("Invalid JSON Schema Type");
                return false;
        }
        return false;
    }

    public static ModelData getModelForArrayType(final Property property) {
        if( JsonDataTypes.ARRAY != property.getType()) {
            log.error("Property {} is not an array!", property.getPropertyName());
            return ModelData.builder().build();
        }

        final Optional<ArrayItems> arrayItems = property.getItems();
        if(!arrayItems.isPresent()) {
            log.warn("No array items definition found");
            return ModelData.builder().build();
        } else {
            final ArrayItems items = arrayItems.get();
            if( JsonDataTypes.OBJECT != items.getType()) {
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
                    final MemberVariableData memberVariableData = simplePropertyToMember(prop);
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

    public static MemberVariableData getMemberFromArrayType(final Property property) {
        final MemberVariableData.MemberVariableDataBuilder memberBuilder = MemberVariableData.builder();
        property.getItems().ifPresent(arrayItems -> {
            final StringBuilder arrayDataType = new StringBuilder();
            if( arrayItems.getType() == JsonDataTypes.OBJECT) {
                //TODO implement
                throw new NotImplementedException();
            } else {
                arrayDataType.append(arrayItems.getType().toString());
            }
            memberBuilder
                    .name(property.getPropertyName())
                    .description(property.getDescription())
                    .jsonDataTypes(property.getType())
                    .isArray(true)
                    .dataType(arrayDataType.toString());
        });
        return memberBuilder.build();
    }

    public static JsonSchemaGenData getGeneratorData(final JsonSchema jsonSchema) {

        final List<ModelData> models = new ArrayList<>();
        final List<MemberVariableData> members = new ArrayList<>();

        //Parse all properties
        jsonSchema.getProperties().getPropertyList().stream().forEach(property -> {
            if ( property.getType() == JsonDataTypes.OBJECT) {
                final ModelData newModel = objectPropertyToModel(property);
                models.add(newModel);
                final MemberVariableData.MemberVariableDataBuilder memberBuilder = MemberVariableData.builder();
                memberBuilder
                        .name(property.getPropertyName())
                        .description(property.getDescription())
                        .jsonDataTypes(property.getType())
                        .isArray(false)
                        .innerModel(Optional.of(newModel));
                members.add(memberBuilder.build());
            }
            else if ( property.getType() == JsonDataTypes.ARRAY ) {
                if ( property.getItems().isPresent()) {
                    final ArrayItems arrayItems = property.getItems().get();
                    if (arrayItems.getType() == JsonDataTypes.OBJECT) {
                        final ModelData newModel = getModelForArrayType(property);
                        models.add(newModel);
                        final MemberVariableData.MemberVariableDataBuilder memberVariableDataBuilder = MemberVariableData.builder();
                        memberVariableDataBuilder
                                .name(property.getPropertyName())
                                .description(property.getDescription())
                                .jsonDataTypes(property.getType())
                                .isArray(true)
                                .innerModel(Optional.of(newModel))
                                .dataType(newModel.getModelName());
                        members.add(memberVariableDataBuilder.build());
                    }
                    else
                    {
                        members.add(getMemberFromArrayType(property));
                    }
                }
            }
            else {
                members.add(simplePropertyToMember(property));
            }
        });

        //create main model
        final ModelData.ModelDataBuilder rootModelBuilder = ModelData.builder();
        rootModelBuilder.modelName(jsonSchema.getTitle()).members(members);
        models.add(rootModelBuilder.build());

        final JsonSchemaGenData.JsonSchemaGenDataBuilder schemaGenDataBuilder = JsonSchemaGenData.builder();
        schemaGenDataBuilder
                .name(jsonSchema.getTitle())
                .models(models);
        return schemaGenDataBuilder.build();
    }
}
