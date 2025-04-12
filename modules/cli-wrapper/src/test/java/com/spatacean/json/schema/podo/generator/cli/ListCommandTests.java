package com.spatacean.json.schema.podo.generator.cli;

import com.spatacean.json.schema.podo.generator.cli.commands.subcommands.ListCommand;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simple run of the {@link ListCommand} command
 *
 * @author George Spătăcean
 */
class ListCommandTests {

    @Test
    void runListCommand() {
        new ListCommand().run();
        assertTrue(true);
    }
}
