package net.gspatace.json.schema.podo.generator.cli;

import net.gspatace.json.schema.podo.generator.cli.commands.Main;
import picocli.CommandLine;

public class JsonSchemaPodoGenerator {
    public static void main(String[] args) {
        final CommandLine cmd = new CommandLine(new Main());
        final int exitCode = cmd.execute(args);
        System.exit(exitCode);
    }
}
