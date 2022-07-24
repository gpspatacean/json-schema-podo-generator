package net.gspatace.json.schema.podo.generator.cli;


import net.gspatace.json.schema.podo.generator.cli.commands.subcommands.CustomConfigHelper;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Simple run of the {@link CustomConfigHelper} command
 *
 * @author George Spătăcean
 */
public class CustomConfigHelperTests {

    @Test
    public void runCustomConfigHelperCommand() throws IllegalAccessException {
        final CustomConfigHelper customConfig = new CustomConfigHelper();

        //"magically" set the private generator name
        FieldUtils.writeField(customConfig, "generator", "test-generator", true);

        customConfig.run();
        assertTrue(true);
    }
}
