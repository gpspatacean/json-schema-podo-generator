package net.gspatace.json.schema.podo.generator.langs;

import net.gspatace.json.schema.podo.generator.annotations.CustomProperties;
import net.gspatace.json.schema.podo.generator.annotations.SchemaGenerator;
import net.gspatace.json.schema.podo.generator.base.AbstractGenerator;
import net.gspatace.json.schema.podo.generator.base.BaseOptions;
import picocli.CommandLine;

/**
 * Basic Generator used as Test Bed for unit tests
 * 
 * @author George Spătăcean
 */
@SchemaGenerator(name = "test-generator", description = "Generator use just for unit testing", embeddedResourceLocation = "testgen")
public class TestGenerator extends AbstractGenerator {

    public TestGenerator(final BaseOptions baseOptions){
        super(baseOptions);
    }

    @CustomProperties
    public static class TestGeneratorCustomProperties{
        @CommandLine.Option(names = "-customOptionOne", description = "Custom Option One")
        private String optionOne;

        @CommandLine.Option(names = "-customOptionTwo", description = "Custom Option Two")
        private String optionTwo;
    }
}
