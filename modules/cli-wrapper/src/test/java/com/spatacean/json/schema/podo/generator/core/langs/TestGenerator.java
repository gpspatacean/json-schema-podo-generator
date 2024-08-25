package com.spatacean.json.schema.podo.generator.core.langs;

import com.spatacean.json.schema.podo.generator.core.templating.TemplateFile;
import com.spatacean.json.schema.podo.generator.core.annotations.CustomProperties;
import com.spatacean.json.schema.podo.generator.core.annotations.SchemaGenerator;
import com.spatacean.json.schema.podo.generator.core.base.AbstractGenerator;
import com.spatacean.json.schema.podo.generator.core.base.GeneratorInput;
import picocli.CommandLine;

/**
 * Basic Generator used as Test Bed for unit tests
 *
 * @author George Spătăcean
 */
@SchemaGenerator(name = "test-generator", description = "Generator use just for unit testing", embeddedResourceLocation = "templates")
public class TestGenerator extends AbstractGenerator {

    private final TestGeneratorCustomProperties customProperties = new TestGeneratorCustomProperties();

    public TestGenerator(final GeneratorInput generatorInput) {
        super(generatorInput);
        addTemplateFile(TemplateFile.builder().templateName("testGenerator.mustache").fileExtension("test").build());
        parseCustomOptionsProperties(customProperties);
    }

    @CustomProperties
    public static class TestGeneratorCustomProperties {
        @CommandLine.Option(names = "-customOptionOne", description = "Custom Option One")
        private String optionOne;

        @CommandLine.Option(names = "-customOptionTwo", description = "Custom Option Two")
        private String optionTwo;
    }
}
