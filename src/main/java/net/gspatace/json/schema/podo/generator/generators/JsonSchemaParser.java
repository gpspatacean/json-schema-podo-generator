package net.gspatace.json.schema.podo.generator.generators;

import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.generators.handlers.PropertyHandlerFactory;
import net.gspatace.json.schema.podo.generator.specification.models.JsonSchema;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class used for translation of JSON Schema Objects to Generator Objects,
 * that will be fed as context to the templating engine.
 *
 * @author George Spătăcean
 */
@Slf4j
public class JsonSchemaParser {

    private final JsonSchema jsonSchema;

    public JsonSchemaParser(final JsonSchema jsonSchema) {
        this.jsonSchema = jsonSchema;
    }

    public JsonSchemaGenData getGeneratorData() {

        final List<ModelData> allModels = new ArrayList<>();
        final List<MemberVariableData> members = new ArrayList<>();

        //Parse all properties
        jsonSchema.getProperties().getPropertyList().stream().forEach(property -> {
            final JsonTypePropertyHandler propertyHandler = PropertyHandlerFactory.getForProperty(property);
            final MemberVariableData memberVariableData = propertyHandler.handle(property, allModels);
            members.add(memberVariableData);
        });

        //create main model
        final ModelData.ModelDataBuilder rootModelBuilder = ModelData.builder();
        rootModelBuilder.modelName(jsonSchema.getTitle()).members(members);
        allModels.add(rootModelBuilder.build());

        final JsonSchemaGenData.JsonSchemaGenDataBuilder schemaGenDataBuilder = JsonSchemaGenData.builder();
        schemaGenDataBuilder
                .name(jsonSchema.getTitle())
                .models(allModels);
        return schemaGenDataBuilder.build();
    }
}
