package com.spatacean.json.schema.podo.generator.core.generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spatacean.json.schema.podo.generator.core.specification.JsonDataTypes;
import com.spatacean.json.schema.podo.generator.core.specification.models.JsonSchema;
import com.spatacean.json.schema.podo.generator.core.utils.ObjectMapperFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

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
        assertEquals("Name of the main Schema is \"Product\"", "Product", generatorData.getName());
        assertEquals("Main Schema should have 4 models", 4, generatorData.getModels().size());
        final List<String> models = generatorData.getModels().stream().map(ModelData::getModelName).toList();
        assertTrue("Main Schema should have \"Product\", \"dimensions\", \"review\" models", models.containsAll(Arrays.asList("Product", "dimensions", "review")));
    }

    @Test
    public void testMainObjectProperties() {
        final ModelData productModel = Utils.getModelByName(generatorData, "Product");
        assertEquals("Main product should have 6 properties", 6, productModel.getMembers().size());
    }

    @Test
    public void testSimpleProperty() {
        final MemberVariableData productIdProperty = Utils.getMemberDataByName(Utils.getModelByName(generatorData, "Product"), "productId");
        assertEquals("Property Name", "productId", productIdProperty.getName());
        Assert.assertEquals("Property Type", JsonDataTypes.INTEGER, productIdProperty.getJsonDataTypes());
        assertEquals("Property description", "The unique identifier for a product", productIdProperty.getDescription());
    }

    @Test
    public void testArrayWithSimpleMembersProperty() {
        final MemberVariableData tagsProperty = Utils.getMemberDataByName(Utils.getModelByName(generatorData, "Product"), "tags");
        assertTrue("This should be an array property", tagsProperty.isArray());
        assertEquals("The array should contain simple Strings", "string", tagsProperty.getDataType());
    }

    @Test
    public void testArrayWithComplexMembersProperty() {
        final MemberVariableData reviewsProperty = Utils.getMemberDataByName(Utils.getModelByName(generatorData, "Product"), "reviews");
        assertTrue("This should be an array property", reviewsProperty.isArray() && JsonDataTypes.ARRAY == reviewsProperty.getJsonDataTypes());
        final ModelData innerModelData = reviewsProperty.getInnerModel().get();
        assertEquals("Name of the inner model ", "review", innerModelData.getModelName());
        assertEquals("Inner model should have 2 properties ", 2, innerModelData.getMembers().size());
    }

    @Test
    public void testSubObjectOfSubObject() {
        final MemberVariableData subDimProperty = Utils.getMemberDataByName(Utils.getModelByName(generatorData, "dimensions"), "subDim");
        assertSame("This should be an object property", JsonDataTypes.OBJECT, subDimProperty.getJsonDataTypes());
        final ModelData subDimModel = subDimProperty.getInnerModel().get();
        assertEquals("Name of the inner model ", "subDim", subDimModel.getModelName());
        assertEquals("SubDim inner model should have 2 properties", 2, subDimModel.getMembers().size());
    }

    @Test
    public void testComplexObjectProperty() {
        final MemberVariableData dimensionsProperty = Utils.getMemberDataByName(Utils.getModelByName(generatorData, "Product"), "dimensions");
        assertSame("This should be an Object Property", JsonDataTypes.OBJECT, dimensionsProperty.getJsonDataTypes());
        assertEquals("This property should have itself 4 properties", 4, dimensionsProperty.getInnerModel().get().getMembers().size());
    }
}
