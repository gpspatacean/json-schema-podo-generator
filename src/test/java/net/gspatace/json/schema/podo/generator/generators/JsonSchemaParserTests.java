package net.gspatace.json.schema.podo.generator.generators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.models.JsonSchema;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.gspatace.json.schema.podo.generator.Utils.getSchemaFromResource;
import static net.gspatace.json.schema.podo.generator.utils.ObjectMapperFactory.createDefaultObjectMapper;
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
        final ObjectMapper objectMapper = createDefaultObjectMapper();
        final String jsonSchemaString = getSchemaFromResource(this.getClass().getClassLoader(), "schemas/ComplexSchema.json").get();
        final JsonSchema jsonSchema = objectMapper.readValue(jsonSchemaString, JsonSchema.class);
        generatorData = JsonSchemaParser.getGeneratorData(jsonSchema);
    }

    @Test
    public void testBasicProperties() {
        assertEquals("Name of the main Schema is \"Product\"", "Product", generatorData.getName());
        assertEquals("Main Schema should have 3 models", 3, generatorData.getModels().size());
        final List<String> models = generatorData.getModels().stream().map(ModelData::getModelName).collect(Collectors.toList());
        assertTrue("Main Schema should have Product, dimensions, reviews models", models.containsAll(Arrays.asList("Product", "dimensions", "reviews")));
    }

    @Test
    public void testMainObjectProperties() {
        final ModelData productModel = getModelByName("Product");
        assertEquals("Main product should have 7 properties", 7, productModel.getMembers().size());
    }

    @Test
    public void testSimpleProperty() {
        final MemberVariableData productIdProperty = getMemberDataByName(getModelByName("Product"), "productId");
        assertEquals("Property Name", "productId", productIdProperty.getName());
        assertEquals("Property Type", JsonDataTypes.INTEGER, productIdProperty.getJsonDataTypes());
        assertEquals("Property description", "The unique identifier for a product", productIdProperty.getDescription());
    }

    @Test
    public void testArrayWithSimpleMembersProperty() {
        final MemberVariableData tagsProperty = getMemberDataByName(getModelByName("Product"), "tags");
        assertTrue("This should be an array property", tagsProperty.isArray());
        assertEquals("The array should contain simple Strings", "string", tagsProperty.getDataType());
    }

    @Test
    public void testArrayWithComplexMembersProperty() {
        final MemberVariableData reviewsProperty = getMemberDataByName(getModelByName("Product"), "reviews");
        assertTrue("This should be an array property", reviewsProperty.isArray() && JsonDataTypes.ARRAY == reviewsProperty.getJsonDataTypes());
        final ModelData innerModelData = reviewsProperty.getInnerModel().get();
        assertEquals("Name of the inner model ", "reviews", innerModelData.getModelName());
        assertEquals("Inner model should have 2 properties ", 2, innerModelData.getMembers().size());
    }

    @Test
    public void testComplexObjectProperty() {
        final MemberVariableData dimensionsProperty = getMemberDataByName(getModelByName("Product"), "dimensions");
        assertSame("This should be an Object Property", JsonDataTypes.OBJECT, dimensionsProperty.getJsonDataTypes() );
        assertEquals("This property should have itself 3 properties", 3, dimensionsProperty.getInnerModel().get().getMembers().size());
    }

    private MemberVariableData getMemberDataByName(final ModelData modelData, final String memberName) {
        return modelData.getMembers().stream().filter(member -> member.getName().equalsIgnoreCase(memberName)).findFirst().orElseThrow(IllegalAccessError::new);
    }

    private ModelData getModelByName(final String modelName) {
        return generatorData.getModels().stream().filter(modelData -> modelData.getModelName().equalsIgnoreCase(modelName)).findFirst().orElseThrow(IllegalAccessError::new);
    }
}
