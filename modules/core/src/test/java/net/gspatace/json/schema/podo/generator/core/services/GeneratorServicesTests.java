package net.gspatace.json.schema.podo.generator.core.services;

import net.gspatace.json.schema.podo.generator.core.base.AbstractGenerator;
import net.gspatace.json.schema.podo.generator.core.base.BaseOptions;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

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
public class GeneratorServicesTests {
    private final static String testGeneratorName = "test-generator";
    private final static GeneratorsService generatorService = GeneratorsService.getInstance();

    @Test
    public void testBasicGeneratorData() {
        final List<GeneratorDescription> availableGenerators = generatorService.getAvailableGenerators();
        assertTrue("At least one registered generator", availableGenerators.size() > 0);
        final GeneratorDescription generatorDescription = availableGenerators.stream().filter(genDesc -> genDesc.getName().equals(testGeneratorName)).findFirst().orElseThrow(IllegalStateException::new);
        assertEquals("Name of the registered generator", testGeneratorName, generatorDescription.getName());
        assertEquals("Description of the generator", "Generator use just for unit testing", generatorDescription.getDescription());
    }

    @Test
    public void testGeneratorInstantiation() {
        final String[] testCustomProps = {"-customOptionOne", "custOptOneValue", "-customOptionTwo", "custOptTwoValue"};
        final BaseOptions baseOptions = BaseOptions.builder()
                .generatorName(testGeneratorName)
                .generatorSpecificProperties(testCustomProps)
                .build();
        final AbstractGenerator testGenerator = generatorService.getGeneratorInstance(baseOptions).get();
        assertNotNull("Generator is not null", testGenerator);
        assertEquals("Generator name", "TestGenerator", testGenerator.getClass().getSimpleName());
    }

    @Test
    public void testGeneratorCustomOptions() {
        final Object testGeneratorCustomOptions = generatorService.getCustomOptionsCommand(testGeneratorName).get();
        assertNotNull("Generator Custom options is not null", testGeneratorCustomOptions);
        assertEquals("Generator Custom Options class", "TestGeneratorCustomProperties", testGeneratorCustomOptions.getClass().getSimpleName());
    }

    @Test
    public void testCustomOptionsSerialization() {
        final Set<OptionDescription> expectedOptions = new HashSet<>();
        expectedOptions.add(OptionDescription.builder().name("-customOptionOne").description("Custom Option One").build());
        expectedOptions.add(OptionDescription.builder().name("-customOptionTwo").description("Custom Option Two").build());
        final Set<OptionDescription> actualOptions = generatorService.getSpecificGeneratorOptions(testGeneratorName);
        assertEquals("Generator Custom Options match:", expectedOptions, actualOptions);
    }
}
