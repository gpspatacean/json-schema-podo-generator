package com.spatacean.json.schema.podo.generator.cli.commands.subcommands;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spatacean.json.schema.podo.generator.core.base.*;
import com.spatacean.json.schema.podo.generator.core.services.GeneratorInstantiationException;
import com.spatacean.json.schema.podo.generator.core.services.GeneratorNotFoundException;
import com.spatacean.json.schema.podo.generator.core.services.GeneratorsHandler;
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
            final AbstractGenerator generatorInstance = GeneratorsHandler.getInstance().getGeneratorInstance(buildGeneratorInput());
            final List<ProcessedSourceFile> files = generatorInstance.generate();
            new SourceFilesDiskWriter(files, outputDirectory).writeToDisk();
        } catch (final GeneratorNotFoundException generatorNotFoundException) {
            System.err.printf("Generator [%s] not found.%n", generatorName);
        } catch (final GeneratorInstantiationException instantiationException) {
            System.err.printf("Failed to instantiate generator [%s].%n", generatorName);
        } catch (final JsonProcessingException jsonProcessingException) {
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
