package net.gspatace.json.schema.podo.generator.cli;

import net.gspatace.json.schema.podo.generator.cli.commands.CustomConfigHelper;
import net.gspatace.json.schema.podo.generator.cli.commands.GenerateCommand;
import net.gspatace.json.schema.podo.generator.cli.commands.ListCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "json-schema-podo-generator", description = "Show a list of base commands",
        subcommands = {ListCommand.class,
                GenerateCommand.class,
                CustomConfigHelper.class,
                CommandLine.HelpCommand.class})
public class Main implements Runnable {
    @Override
    public void run() {
        new CommandLine(new Main()).usage(System.out);
    }
}
