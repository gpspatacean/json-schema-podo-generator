package com.spatacean.json.schema.podo.generator.core.generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatacean.json.schema.podo.generator.core.specification.JsonDataTypes;
import com.spatacean.json.schema.podo.generator.core.specification.models.JsonSchema;
import com.spatacean.json.schema.podo.generator.core.utils.ObjectMapperFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tests for {@link JsonSchemaParser}. The tests suite parses a JSON Schema
 * and check various information on the built {@link JsonSchemaGenData}
 *
 * @author George Spătăcean
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
public class JsonSchemaParserTests {
    private final JsonSchemaGenData generatorData;

    public JsonSchemaParserTests() throws JsonProcessingException {
        final ObjectMapper objectMapper = ObjectMapperFactory.createDefaultObjectMapper();
        final String jsonSchemaString = com.spatacean.json.schema.podo.generator.core.Utils.getSchemaFromResource(this.getClass().getClassLoader(), "schemas/ComplexSchema.json").get();
        final JsonSchema jsonSchema = objectMapper.readValue(jsonSchemaString, JsonSchema.class);
        generatorData = new JsonSchemaParser(jsonSchema).getGeneratorData();
    }

    @Test
    public void testBasicProperties() {
        assertEquals("Product", generatorData.getName(), "Name of the main Schema is \"Product\"");
        assertEquals(4, generatorData.getModels().size(), "Main Schema should have 4 models");
        final List<String> models = generatorData.getModels().stream().map(ModelData::getModelName).toList();
        assertTrue(models.containsAll(Arrays.asList("Product", "dimensions", "review")), "Main Schema should have \"Product\", \"dimensions\", \"review\" models");
    }

    @Test
    public void testMainObjectProperties() {
        final ModelData productModel = Utils.getModelByName(generatorData, "Product");
        assertEquals(6, productModel.getMembers().size(), "Main product should have 6 properties");
    }

    @Test
    public void testSimpleProperty() {
        final MemberVariableData productIdProperty = Utils.getMemberDataByName(Utils.getModelByName(generatorData, "Product"), "productId");
        assertEquals("productId", productIdProperty.getName(), "Property Name");
        assertEquals(JsonDataTypes.INTEGER, productIdProperty.getJsonDataTypes(), "Property Type");
        assertEquals("The unique identifier for a product", productIdProperty.getDescription(), "Property description");
    }

    @Test
    public void testArrayWithSimpleMembersProperty() {
        final MemberVariableData tagsProperty = Utils.getMemberDataByName(Utils.getModelByName(generatorData, "Product"), "tags");
        assertTrue(tagsProperty.isArray(), "This should be an array property");
        assertEquals("string", tagsProperty.getDataType(), "The array should contain simple Strings");
    }

    @Test
    public void testArrayWithComplexMembersProperty() {
        final MemberVariableData reviewsProperty = Utils.getMemberDataByName(Utils.getModelByName(generatorData, "Product"), "reviews");
        assertTrue(reviewsProperty.isArray() && JsonDataTypes.ARRAY == reviewsProperty.getJsonDataTypes(), "This should be an array property");
        final ModelData innerModelData = reviewsProperty.getInnerModel().get();
        assertEquals("review", innerModelData.getModelName(), "Name of the inner model ");
        assertEquals(2, innerModelData.getMembers().size(), "Inner model should have 2 properties ");
    }

    @Test
    public void testSubObjectOfSubObject() {
        final MemberVariableData subDimProperty = Utils.getMemberDataByName(Utils.getModelByName(generatorData, "dimensions"), "subDim");
        assertSame(JsonDataTypes.OBJECT, subDimProperty.getJsonDataTypes(), "This should be an object property");
        final ModelData subDimModel = subDimProperty.getInnerModel().get();
        assertEquals("subDim", subDimModel.getModelName(), "Name of the inner model ");
        assertEquals(2, subDimModel.getMembers().size(), "SubDim inner model should have 2 properties");
    }

    @Test
    public void testComplexObjectProperty() {
        final MemberVariableData dimensionsProperty = Utils.getMemberDataByName(Utils.getModelByName(generatorData, "Product"), "dimensions");
        assertSame(JsonDataTypes.OBJECT, dimensionsProperty.getJsonDataTypes(), "This should be an Object Property");
        assertEquals(4, dimensionsProperty.getInnerModel().get().getMembers().size(), "This property should have itself 4 properties");
    }
}
