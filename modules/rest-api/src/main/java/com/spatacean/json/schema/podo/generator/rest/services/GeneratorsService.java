package com.spatacean.json.schema.podo.generator.rest.services;

import lombok.extern.slf4j.Slf4j;
import com.spatacean.json.schema.podo.generator.core.base.AbstractGenerator;
import com.spatacean.json.schema.podo.generator.core.base.GeneratorInput;
import com.spatacean.json.schema.podo.generator.core.base.ProcessedSourceFile;
import com.spatacean.json.schema.podo.generator.core.base.SourceFilesArchiveBuilder;
import com.spatacean.json.schema.podo.generator.core.services.GeneratorDescription;
import com.spatacean.json.schema.podo.generator.core.services.GeneratorsHandler;
import com.spatacean.json.schema.podo.generator.core.services.OptionDescription;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * Class for delegating work from
 * REST Controller to core logic.
 *
 * @author George Spătăcean
 */
@Slf4j
@Service
public class GeneratorsService {

    private final GeneratorsHandler generatorsHandler = GeneratorsHandler.getInstance();

    public List<GeneratorDescription> listGenerators() {
        return generatorsHandler.getAvailableGenerators();
    }

    public GeneratorDescription listGeneratorDescription(@NotNull final String generatorName) {
        return generatorsHandler.getGeneratorDescription(generatorName);
    }

    public Set<OptionDescription> listGeneratorProperties(@NotNull final String generatorName) {
        return generatorsHandler.getSpecificGeneratorOptions(generatorName);
    }

    public InputStreamResource buildCodeArchive(@NotNull final String generatorName,
                                                @NotNull final String[] options,
                                                @NotNull final String schema) throws IOException {
        final GeneratorInput generatorInput = GeneratorInput.builder()
                .generatorName(generatorName)
                .inputSpec(schema)
                .generatorSpecificProperties(options)
                .build();
        final AbstractGenerator generatorInstance = GeneratorsHandler.getInstance().getGeneratorInstance(generatorInput);
        final List<ProcessedSourceFile> processedSources = generatorInstance.generate();
        final byte[] archive = new SourceFilesArchiveBuilder(processedSources).buildArchive();
        return new InputStreamResource(new ByteArrayInputStream(archive));
    }
}
