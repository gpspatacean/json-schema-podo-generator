package net.gspatace.json.schema.podo.generator.cli.commands.subcommands;

import net.gspatace.json.schema.podo.generator.core.services.CustomOptionsInstantiationException;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorNotFoundException;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorsHandler;
import picocli.CommandLine;

import java.util.Comparator;
import java.util.Optional;

@CommandLine.Command(name = "config-help", description = "Show generator specific options")
public class CustomConfigHelper implements Runnable {

    @CommandLine.Option(names = "-g", required = true)
    private String generator;

    @Override
    public void run() {
        try {
            final Optional<Object> optionsCommand = GeneratorsHandler.getInstance().getCustomOptionsCommand(generator);
            if (optionsCommand.isPresent()) {
                CommandLine cmd = new CommandLine(optionsCommand.get());
                cmd.setHelpFactory(new HelpCustomizationFactory());
                cmd.usage(System.out);
            } else {
                System.out.printf("Generator '%s' does not have custom options.%n", generator);
            }
        } catch (final CustomOptionsInstantiationException optionsInstantiationException) {
            System.err.printf("Failed to instantiate CustomOptions for generator '%s'.%n", generator);
        } catch (final GeneratorNotFoundException generatorNotFoundException) {
            System.err.printf("Generator '%s' not found, please check available generators.%n", generator);
        }
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
