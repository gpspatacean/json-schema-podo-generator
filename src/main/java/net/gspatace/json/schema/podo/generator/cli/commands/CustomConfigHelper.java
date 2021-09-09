package net.gspatace.json.schema.podo.generator.cli.commands;

import net.gspatace.json.schema.podo.generator.services.GeneratorsService;
import picocli.CommandLine;

import java.util.Comparator;
import java.util.Optional;

@CommandLine.Command(name = "config-help", description = "Show generator specific options")
public class CustomConfigHelper implements Runnable {

    @CommandLine.Option(names = "-g", required = true)
    private String generator;

    @Override
    public void run() {
        final Optional<Object> optionsCommand = GeneratorsService.getInstance().getCustomOptionsCommand(generator);
        if (optionsCommand.isPresent()) {
            CommandLine cmd = new CommandLine(optionsCommand.get());
            cmd.setHelpFactory(new HelpCustomizationFactory());
            cmd.usage(System.out);
            return;
        }
        System.err.println("generator not found");
    }

    static class HelpCustomizationFactory implements CommandLine.IHelpFactory {

        @Override
        public CommandLine.Help create(CommandLine.Model.CommandSpec commandSpec, CommandLine.Help.ColorScheme colorScheme) {
            return new CommandLine.Help(commandSpec, colorScheme) {

                @Override
                public String synopsisHeading(Object... params) {
                    return "Usage: generate ... -genopts ";
                }

                @Override
                public String detailedSynopsis(int synopsisHeadingLength, Comparator<CommandLine.Model.OptionSpec> optionSort, boolean clusterBooleanOptions) {
                    return "<specific-options>\n";
                }
            };
        }
    }
}
