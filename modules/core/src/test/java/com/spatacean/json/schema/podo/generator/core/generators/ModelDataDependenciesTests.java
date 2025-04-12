package com.spatacean.json.schema.podo.generator.core.generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatacean.json.schema.podo.generator.core.Utils;
import com.spatacean.json.schema.podo.generator.core.specification.models.JsonSchema;
import com.spatacean.json.schema.podo.generator.core.utils.ObjectMapperFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.spatacean.json.schema.podo.generator.core.generators.Utils.getModelByName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for dependencies of a {@link ModelData}
 * A dependency is another Model that the current Model depends.
 * This can translate, further down the line, in {@code #include<"SomeSubObject.hpp"} in C++
 * or {@code import foo.bar.SomeSubObject;} in Java
 *
 * @author George Spătăcean
 */
class ModelDataDependenciesTests {
    private final JsonSchemaGenData generatorData;

    public ModelDataDependenciesTests() throws JsonProcessingException {
        final ObjectMapper objectMapper = ObjectMapperFactory.createDefaultObjectMapper();
        final String jsonSchemaString = Utils.getSchemaFromResource(this.getClass().getClassLoader(), "schemas/ComplexSchema.json").get();
        final JsonSchema jsonSchema = objectMapper.readValue(jsonSchemaString, JsonSchema.class);
        generatorData = new JsonSchemaParser(jsonSchema).getGeneratorData();
    }

    @Test
    void testObjectDependency() {
        final ModelData productModel = getModelByName(generatorData, "Product");
        final List<Map<String, String>> dependencies = productModel.getDependencies();
        assertEquals(2, dependencies.size(), "Product must have 2 dependency, \"dimensions\"");
        final boolean hasCorrectDependencies = dependencies
                .stream()
                .map(Map::values)
                .map(ArrayList::new)
                .reduce(new ArrayList<>(), (acc, singleElementList) -> {
                    acc.add(singleElementList.get(0));
                    return acc;
                })
                .containsAll(Arrays.asList("Dimensions", "Review"));
        assertTrue(hasCorrectDependencies, "Product must have correct dependencies");
    }
}
