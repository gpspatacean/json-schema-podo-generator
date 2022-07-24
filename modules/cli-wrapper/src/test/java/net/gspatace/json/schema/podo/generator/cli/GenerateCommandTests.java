package net.gspatace.json.schema.podo.generator.cli;

import net.gspatace.json.schema.podo.generator.cli.commands.subcommands.GenerateCommand;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.After;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertTrue;

/**
 * Basic test over the Generate command. It runs the Test Generator,
 * which renders a very basic template, but passes throughout a most of the workflow
 *
 * @author George Spătăcean
 */
public class GenerateCommandTests {

    @Test
    public void runGenerateCommandWithTestGenerator() throws IllegalAccessException {
        final GenerateCommand generateCommand = new GenerateCommand();

        //"magically" set the private fields.
        FieldUtils.writeField(generateCommand, "generatorName", "test-generator", true);
        FieldUtils.writeField(generateCommand, "inputFile", "test/inputTestSchema.json", true);
        FieldUtils.writeField(generateCommand, "outputDirectory", "target/gen-output", true);

        final Map<String, String> genProps = new HashMap<>();
        genProps.put("-customOptionOne", "custOptOneValue");
        genProps.put("-customOptionTwo", "custOptTwoValue");
        FieldUtils.writeField(generateCommand, "generatorSpecificProperties", genProps, true);

        generateCommand.run();
        assertTrue(true);
    }

    @After
    public void cleanupTestOutput() {
        //TODO
    }
}
