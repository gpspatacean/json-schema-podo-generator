package net.gspatace.json.schema.podo.generator.rest.controllers;

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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Main Wrapper over json-schema-podo-generator
 * core logic.
 * Exposes APIs for
 * <ul>
 *     <li>Generators listing</li>
 *     <li>Specific Generator options</li>
 *     <li>Generate archived code based on input</li>
 * </ul>
 *
 * @author George Spătăcean
 */
@RestController
@CrossOrigin
@RequestMapping("/generators")
@Slf4j
public class GeneratorController {

    @GetMapping(value = "",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<GeneratorDescription> listGenerator() {
        return GeneratorsHandler.getInstance().getAvailableGenerators();
    }

    @GetMapping(value = "/{generatorName}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<OptionDescription> listGeneratorProperties(@PathVariable final String generatorName) {
        return GeneratorsHandler.getInstance().getSpecificGeneratorOptions(generatorName);
    }

    @PostMapping(value = "/{generatorName}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<Resource> serveGeneratedCodeArchive(@PathVariable final String generatorName,
                                                              @RequestParam final String options,
                                                              @RequestParam final MultipartFile schema,
                                                              @RequestParam(defaultValue = "json-schema-generated-podos") final String archiveName)
            throws IOException, GeneratorNotFoundException {
        final String fileContents = new String(schema.getBytes());
        return buildResponseEntity(generatorName, options, fileContents, archiveName);
    }

    @PostMapping(value = "/{generatorName}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<Resource> serveGeneratedCodeArchiveFromString(@PathVariable final String generatorName,
                                                                        @RequestParam final String options,
                                                                        @RequestBody final String schema,
                                                                        @RequestParam(defaultValue = "json-schema-generated-podos") final String archiveName)
            throws IOException, GeneratorNotFoundException {
        return buildResponseEntity(generatorName, options, schema, archiveName);
    }

    private ResponseEntity<Resource> buildResponseEntity(final String generatorName,
                                                         final String options,
                                                         final String schema,
                                                         final String archiveName)
            throws GeneratorNotFoundException, IOException {
        final GeneratorInput generatorInput = GeneratorInput.builder()
                .generatorName(generatorName)
                .inputSpec(schema)
                .generatorSpecificProperties(formatGeneratorOptionsInput(options))
                .build();
        final AbstractGenerator generatorInstance = GeneratorsHandler.getInstance().getGeneratorInstance(generatorInput);
        final List<ProcessedSourceFile> processedSources = generatorInstance.generate();
        final byte[] archive = new SourceFilesArchiveBuilder(processedSources).buildArchive();
        final InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(archive));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archiveName + ".zip\"")
                .body(resource);
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
        } catch (JsonProcessingException e) {
            log.error("Failed to map specific options, these will be defaulted:", e);
        }
        return new String[0];
    }
}
