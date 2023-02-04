package net.gspatace.json.schema.podo.generator.rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.core.base.AbstractGenerator;
import net.gspatace.json.schema.podo.generator.core.base.GeneratorInput;
import net.gspatace.json.schema.podo.generator.core.base.ProcessedSourceFile;
import net.gspatace.json.schema.podo.generator.core.base.SourceFilesArchiveBuilder;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorDescription;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorNotFoundException;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorsHandler;
import net.gspatace.json.schema.podo.generator.core.services.OptionDescription;
import net.gspatace.json.schema.podo.generator.rest.models.CustomOption;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
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

    public Set<OptionDescription> listGeneratorProperties(@NotNull final String generatorName) throws GeneratorNotFoundException {
        return generatorsHandler.getSpecificGeneratorOptions(generatorName);
    }

    public InputStreamResource buildCodeArchive(@NotNull final String generatorName,
                                                @NotNull final String options,
                                                @NotNull final String schema) throws GeneratorNotFoundException, IOException {
        final GeneratorInput generatorInput = GeneratorInput.builder()
                .generatorName(generatorName)
                .inputSpec(schema)
                .generatorSpecificProperties(formatGeneratorOptionsInput(options))
                .build();
        final AbstractGenerator generatorInstance = GeneratorsHandler.getInstance().getGeneratorInstance(generatorInput);
        final List<ProcessedSourceFile> processedSources = generatorInstance.generate();
        final byte[] archive = new SourceFilesArchiveBuilder(processedSources).buildArchive();
        return new InputStreamResource(new ByteArrayInputStream(archive));
    }

    private String[] formatGeneratorOptionsInput(final String options) {
        final ObjectMapper objectMapper = new ObjectMapper();
        try {
            final List<CustomOption> customOptions = objectMapper.readValue(options, new TypeReference<List<CustomOption>>() {
            });
            final List<String> propList = new ArrayList<>();
            customOptions.forEach(option -> {
                propList.add(option.getName());
                propList.add(option.getValue());
            });
            return propList.toArray(new String[0]);
        } catch (final JsonProcessingException e) {
            log.error("Failed to map specific options, these will be defaulted:", e);
        }
        return new String[0];
    }
}
