package net.gspatace.json.schema.podo.generator.cli.commands;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.gspatace.json.schema.podo.generator.base.AbstractGenerator;
import net.gspatace.json.schema.podo.generator.base.BaseOptions;
import net.gspatace.json.schema.podo.generator.services.GeneratorsService;
import picocli.CommandLine;

import java.util.*;

@CommandLine.Command(name = "generate", description = "Run the generator",
        subcommands = {CommandLine.HelpCommand.class})
public class GenerateCommand implements Runnable {

    @CommandLine.Option(names = "-g", required = true)
    protected String generatorName;

    @CommandLine.Option(names = "-i", required = true)
    protected String inputFile;

    @CommandLine.Option(names = "-o")
    protected String outputDirectory;

    @CommandLine.Option(names = "-genprops", split = ",")
    Map<String, String> generatorSpecificProperties = new HashMap<>();

    @Override
    public void run() {
        final Optional<AbstractGenerator> generatorInstance = GeneratorsService.getInstance().getGeneratorInstance(getOptions());
        generatorInstance.ifPresent(instance -> {
            try {
                instance.generate();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
    }

    private BaseOptions getOptions() {
        final BaseOptions.BaseOptionsBuilder builder = BaseOptions.builder();
        return builder
                .generatorName(generatorName)
                .inputSpec(inputFile)
                .outputDirectory(outputDirectory)
                .generatorSpecificProperties(getUnifiedGeneratorSpecificProperties())
                .build();
    }

    private String[] getUnifiedGeneratorSpecificProperties() {
        final List<String> propList = new ArrayList<>();
        generatorSpecificProperties.forEach((k,v)->{
            propList.add(k);
            propList.add(v);
        });
        return propList.toArray(new String[0]);
    }
}
