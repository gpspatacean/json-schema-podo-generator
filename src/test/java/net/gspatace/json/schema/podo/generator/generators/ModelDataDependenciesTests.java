package net.gspatace.json.schema.podo.generator.generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gspatace.json.schema.podo.generator.specification.models.JsonSchema;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static net.gspatace.json.schema.podo.generator.Utils.getSchemaFromResource;
import static net.gspatace.json.schema.podo.generator.generators.Utils.getModelByName;
import static net.gspatace.json.schema.podo.generator.utils.ObjectMapperFactory.createDefaultObjectMapper;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for dependencies of a {@link ModelData}
 * A dependency is another Model that the current Model depends.
 * This can translate, further down the line, in {@code #include<"SomeSubObject.hpp"} in C++
 * or {@code import foo.bar.SomeSubObject;} in Java
 *
 * @author George Spătăcean
 */
public class ModelDataDependenciesTests {
    private final JsonSchemaGenData generatorData;

    public ModelDataDependenciesTests() throws JsonProcessingException {
        final ObjectMapper objectMapper = createDefaultObjectMapper();
        final String jsonSchemaString = getSchemaFromResource(this.getClass().getClassLoader(), "schemas/ComplexSchema.json").get();
        final JsonSchema jsonSchema = objectMapper.readValue(jsonSchemaString, JsonSchema.class);
        generatorData = new JsonSchemaParser(jsonSchema).getGeneratorData();
    }

    @Test
    public void testObjectDependency(){
        final ModelData productModel = getModelByName(generatorData,"Product");
        final List<Map<String, String>> dependencies = productModel.getDependencies();
        assertEquals("Product must have 2 dependency, \"dimensions\"", 2, dependencies.size());
        final boolean hasCorrectDependencies = dependencies
                .stream()
                .map(Map::values)
                .map(ArrayList::new)
                .reduce(new ArrayList<>(), (acc, singleElementList) -> {
                    acc.add(singleElementList.get(0));
                    return acc;
                })
                .containsAll(Arrays.asList("Dimensions", "Reviews"));
        assertTrue("Product must have correct dependencies", hasCorrectDependencies);
    }
}
