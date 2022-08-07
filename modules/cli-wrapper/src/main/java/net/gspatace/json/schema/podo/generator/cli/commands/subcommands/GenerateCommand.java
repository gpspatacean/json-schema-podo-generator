package net.gspatace.json.schema.podo.generator.cli.commands.subcommands;

import com.fasterxml.jackson.core.JsonProcessingException;
import net.gspatace.json.schema.podo.generator.core.base.*;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorNotFoundException;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorsService;
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
        try {
            final AbstractGenerator generatorInstance = GeneratorsService.getInstance().getGeneratorInstance(buildGeneratorInput());
            final List<ProcessedSourceFile> files = generatorInstance.generate();
            new SourceFilesDiskWriter(files, outputDirectory).writeToDisk();
        } catch ( GeneratorNotFoundException generatorNotFoundException ){
            System.err.printf("Generator [%s] not found.%n", generatorName);
        } catch ( JsonProcessingException jsonProcessingException) {
            System.err.println("Failed to process provided JSON schema");
        }
    }

    private GeneratorInput buildGeneratorInput() {
        final String schemaInput = SchemaReader.getSchema(inputFile);
        final GeneratorInput.GeneratorInputBuilder builder = GeneratorInput.builder();
        return builder
                .generatorName(generatorName)
                .inputSpec(schemaInput)
                .generatorSpecificProperties(getUnifiedGeneratorSpecificProperties())
                .build();
    }

    private String[] getUnifiedGeneratorSpecificProperties() {
        final List<String> propList = new ArrayList<>();
        generatorSpecificProperties.forEach((k, v) -> {
            propList.add(k);
            propList.add(v);
        });
        return propList.toArray(new String[0]);
    }
}
