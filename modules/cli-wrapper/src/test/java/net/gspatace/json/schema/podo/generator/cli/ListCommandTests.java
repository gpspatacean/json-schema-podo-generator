package net.gspatace.json.schema.podo.generator.cli;

import net.gspatace.json.schema.podo.generator.cli.commands.subcommands.ListCommand;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Simple run of the {@link ListCommand} command
 *
 * @author George Spătăcean
 */
public class ListCommandTests {

    @Test
    public void runListCommand(){
        new ListCommand().run();
        assertTrue(true);
    }
}
