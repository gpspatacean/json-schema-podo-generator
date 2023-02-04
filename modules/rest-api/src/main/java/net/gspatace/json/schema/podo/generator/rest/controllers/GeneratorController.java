package net.gspatace.json.schema.podo.generator.rest.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorDescription;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorNotFoundException;
import net.gspatace.json.schema.podo.generator.core.services.OptionDescription;
import net.gspatace.json.schema.podo.generator.rest.services.GeneratorsService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * REST Controller over json-schema-podo-generator
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
@RequiredArgsConstructor
public class GeneratorController {

    private final GeneratorsService service;

    @GetMapping(value = "",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public List<GeneratorDescription> listGenerators() {
        return service.listGenerators();
    }

    @GetMapping(value = "/{generatorName}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public Set<OptionDescription> listGeneratorProperties(@PathVariable final String generatorName) throws GeneratorNotFoundException {
        return service.listGeneratorProperties(generatorName);
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
        final InputStreamResource archive = service.buildCodeArchive(generatorName, options, schema);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archiveName + ".zip\"")
                .body(archive);
    }
}
