package com.spatacean.json.schema.podo.generator.cli;


import com.spatacean.json.schema.podo.generator.cli.commands.subcommands.CustomConfigHelper;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
