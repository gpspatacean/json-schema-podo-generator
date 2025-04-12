package com.spatacean.json.schema.podo.generator.core.services;

import com.spatacean.json.schema.podo.generator.core.base.AbstractGenerator;
import com.spatacean.json.schema.podo.generator.core.base.GeneratorInput;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basic tests of the generator services, things like
 * <ul>
 *     <li>list of registered generators</li>
 *     <li>generator instantiation</li>
 *     <li>custom options instantiation</li>
 * </ul>
 *
 * @author George Spătăcean
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
class GeneratorsHandlerTests {
    private static final String testGeneratorName = "test-generator";
    private static final GeneratorsHandler generatorService = GeneratorsHandler.getInstance();

    @Test
    void testBasicGeneratorData() {
        final List<GeneratorDescription> availableGenerators = generatorService.getAvailableGenerators();
        assertTrue(availableGenerators.size() > 0, "At least one registered generator");
        final GeneratorDescription generatorDescription = generatorService.getGeneratorDescription(testGeneratorName);
        assertEquals(testGeneratorName, generatorDescription.name(), "Name of the registered generator");
        assertEquals("Generator use just for unit testing", generatorDescription.description(), "Description of the generator");
    }

    @Test
    void testGeneratorInstantiation() {
        final String[] testCustomProps = {"-customOptionOne", "custOptOneValue", "-customOptionTwo", "custOptTwoValue"};
        final GeneratorInput generatorInput = GeneratorInput.builder()
                .generatorName(testGeneratorName)
                .generatorSpecificProperties(testCustomProps)
                .build();
        final AbstractGenerator testGenerator = generatorService.getGeneratorInstance(generatorInput);
        assertNotNull(testGenerator, "Generator is not null");
        assertEquals("TestGenerator", testGenerator.getClass().getSimpleName(), "Generator name");
    }

    @Test
    void testGeneratorCustomOptions() {
        final Object testGeneratorCustomOptions = generatorService.getCustomOptionsCommand(testGeneratorName).get();
        assertNotNull(testGeneratorCustomOptions, "Generator Custom options is not null");
        assertEquals("TestGeneratorCustomProperties", testGeneratorCustomOptions.getClass().getSimpleName(), "Generator Custom Options class");
    }

    @Test
    void testGeneratorDescriptionRetrieval() {
        assertThrows(GeneratorNotFoundException.class, () -> generatorService.getGeneratorDescription("non-existent"));
    }

    @Test
    void testCustomOptionsSerialization() throws GeneratorNotFoundException {
        final Set<OptionDescription> expectedOptions = new HashSet<>();
        expectedOptions.add(OptionDescription.builder().name("-customOptionOne").description("Custom Option One").build());
        expectedOptions.add(OptionDescription.builder().name("-customOptionTwo").description("Custom Option Two").build());
        final Set<OptionDescription> actualOptions = generatorService.getSpecificGeneratorOptions(testGeneratorName);
        assertEquals(expectedOptions, actualOptions, "Generator Custom Options match:");
    }
}
