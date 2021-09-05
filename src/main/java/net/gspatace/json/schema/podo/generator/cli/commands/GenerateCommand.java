package net.gspatace.json.schema.podo.generator.cli.commands;

import picocli.CommandLine;

@CommandLine.Command(name = "generate", description = "Run the generator",
        subcommands = {CommandLine.HelpCommand.class})
public class GenerateCommand implements Runnable {

    @CommandLine.Option(names = "-g", required = true)
    protected String generatorName;

    @CommandLine.Option(names = "-i", required = true)
    protected String inputFile;

    @Override
    public void run() {
        System.out.println("Not implemented yet!");
    }
}
