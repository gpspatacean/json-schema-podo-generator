package net.gspatace.json.schema.podo.generator.cli.commands.subcommands;

import net.gspatace.json.schema.podo.generator.core.services.GeneratorDescription;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorsService;
import picocli.CommandLine;

import java.util.List;

@CommandLine.Command(name = "list", description = "List available generators")
public class ListCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("List of available generators:");
        System.out.println("Name\t\tDescription");
        final String pattern = "%s\t\t%s%n";
        final List<GeneratorDescription> availGenerators = GeneratorsService.getInstance().getAvailableGenerators();
        availGenerators.forEach(description -> System.out.printf(pattern, description.getName(), description.getDescription()));
    }
}
