package com.spatacean.json.schema.podo.generator.cli.commands;

import com.spatacean.json.schema.podo.generator.cli.commands.subcommands.CustomConfigHelper;
import com.spatacean.json.schema.podo.generator.cli.commands.subcommands.GenerateCommand;
import com.spatacean.json.schema.podo.generator.cli.commands.subcommands.ListCommand;
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
