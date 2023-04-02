package net.gspatace.json.schema.podo.generator.rest.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.Explode;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.ParameterStyle;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorDescription;
import net.gspatace.json.schema.podo.generator.core.services.GeneratorNotFoundException;
import net.gspatace.json.schema.podo.generator.core.services.OptionDescription;
import net.gspatace.json.schema.podo.generator.rest.adapter.CustomOptionsTransformer;
import net.gspatace.json.schema.podo.generator.rest.models.ProblemDetails;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
@Tag(name = "PODO Generators Actions", description = "REST API over podo-generator-core logic")
public class GeneratorController {

    private final GeneratorsService service;

    private final CustomOptionsTransformer optionsTransformer;

    /**
     * Endpoint handler for "/".
     * It returns the list of the available generators
     *
     * @return list of available generators
     */
    @Operation(
            description = "Retrieve all available generators",
            summary = "Retrieve generators",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Operation",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = GeneratorDescription.class)),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "[{\"name\":\"cpp\",\"description\":\"Generate C++ PODOs\"},{\"name\":\"java\",\"description\":\"Java POJOs.\"}]"
                                                    )}
                                    )}
                    )
            })
    @GetMapping(value = "")
    public List<GeneratorDescription> listGenerators() {
        return service.listGenerators();
    }

    /**
     * Endpoint handler for /{generatorName}
     *
     * @param generatorName the target generator
     * @return generator details
     */
    @Operation(
            description = "Retrieve details of a registered generator",
            summary = "Retrieve generator",
            parameters = {
                    @Parameter(
                            name = "generatorName",
                            in = ParameterIn.PATH,
                            description = "name of the generator",
                            example = "cpp"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Operation",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = GeneratorDescription.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\"name\":\"cpp\",\"description\":\"Generate C++ PODOs\"}"
                                                    )}
                                    )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Generator not found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetails.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\"title\":\"Generator not found.\",\"status\":404,\"details\":\"Generator 'javas' not found\",\"instance\":\"/generators/javas\"}"
                                                    )}
                                    )}
                    )}
    )
    @GetMapping(value = "/{generatorName}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<GeneratorDescription> getGeneratorDetails(@PathVariable final String generatorName) {
        return ResponseEntity.ok(service.listGeneratorDescription(generatorName));
    }

    /**
     * Endpoint handler for "/{generatorName}/options"
     *
     * @param generatorName the target generator
     * @return list of custom specific generator options
     */
    @Operation(
            summary = "Retrieve generator options",
            description = "Retrieve the list of generator options that can be configured",
            parameters = {
                    @Parameter(
                            name = "generatorName",
                            in = ParameterIn.PATH,
                            description = "name of the generator",
                            example = "cpp"
                    )
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful Operation",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            array = @ArraySchema(schema = @Schema(implementation = OptionDescription.class)),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "[{\"name\":\"-ns\",\"description\":\"Namespace of the generated PODOs. Defaults to \\\"podo_generator\\\"\",\"defaultValue\":\"podo_generator\"},{\"name\":\"-l\",\"description\":\"Name of the module. Defaults to \\\"generatedPodos\\\"\",\"defaultValue\":\"generatedPodos\"}]"
                                                    )}
                                    )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Generator not found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetails.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\"title\":\"Generator not found.\",\"status\":404,\"details\":\"Generator 'javas' not found\",\"instance\":\"/generators/javas\"}"
                                                    )}
                                    )}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetails.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\"title\":\"Custom Properties Object Instantiation error.\",\"status\":500,\"details\":\"Failed to instantiate Custom Properties object of class 'CppSpecificProperties' for generator `cpp`\",\"instance\":\"/generators/cpp/options\"}"
                                                    )}
                                    )}
                    )
            })
    @GetMapping(value = "/{generatorName}/options")
    public ResponseEntity<Set<OptionDescription>> listGeneratorProperties(@PathVariable final String generatorName) {
        return ResponseEntity.ok(service.listGeneratorProperties(generatorName));
    }

    /**
     * Endpoint handler for "/{generatorName}
     * It generates an archive with PODOs, based on
     * input schema and options
     *
     * @param generatorName  target generator
     * @param allQueryParams custom specific generator options
     * @param schema         the input JSON Schema
     * @param archiveName    name of the generated code archive
     * @return the generated code archive
     * @throws IOException
     * @throws GeneratorNotFoundException in case the generator was not found
     */
    @Operation(
            summary = "Generate code",
            description = "Generate code from a JSON Schema input",
            parameters = {
                    @Parameter(
                            name = "generatorName",
                            in = ParameterIn.PATH,
                            description = "name of the generator",
                            example = "java"
                    ),
                    @Parameter(
                            name = "options",
                            description = "Name (without leading dashes) and value pairs for custom options of this generator",
                            in = ParameterIn.QUERY,
                            style = ParameterStyle.FORM,
                            schema = @Schema(type = "object"),
                            explode = Explode.TRUE,
                            example = "{\"package\":\"generated.api\",\"version\":\"1.0.0-SNAPSHOT\"}"
                    ),
                    @Parameter(
                            name = "archiveName",
                            in = ParameterIn.PATH,
                            description = "name of the generated archive",
                            example = "json-schema-generated-podos"
                    )
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "JSON Schema",
                    content = {
                            @Content(
                                    examples = {
                                            @ExampleObject(
                                                    name = "Class with two fields",
                                                    description = "This schema defines a \"Product\" object, with 2 fields - " +
                                                            "\"productId\" as an integer and \"productName\" as a string",
                                                    summary = "Class with two fields",
                                                    value = "{\"$schema\":\"https://json-schema.org/draft/2020-12/schema\",\"$id\":\"https://example.com/product.schema.json\",\"title\":\"Product\",\"description\":\"A product from Acme's catalog\",\"type\":\"object\",\"properties\":{\"productId\":{\"description\":\"The unique identifier for a product\",\"type\":\"integer\"},\"productName\":{\"description\":\"Name of the product\",\"type\":\"string\"}}}"
                                            )
                                    },
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            ),
                            @Content(
                                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE
                            )
                    }
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE
                                    )}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Generator not found",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetails.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\"title\":\"Generator not found.\",\"status\":404,\"details\":\"Generator 'javas' not found\",\"instance\":\"/generators/javas\"}"
                                                    )}
                                    )}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error",
                            content = {
                                    @Content(
                                            mediaType = MediaType.APPLICATION_PROBLEM_JSON_VALUE,
                                            schema = @Schema(implementation = ProblemDetails.class),
                                            examples = {
                                                    @ExampleObject(
                                                            value = "{\"title\":\"Custom Properties Object Instantiation error.\",\"status\":500,\"details\":\"Failed to instantiate Custom Properties object of class 'CppSpecificProperties' for generator `cpp`\",\"instance\":\"/generators/cpp/options\"}"
                                                    )}
                                    )}
                    )
            }
    )
    @PostMapping(value = "/{generatorName}",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<Resource> generateFromMultipartFile(@PathVariable final String generatorName,
                                                              @RequestParam(required = false) final Map<String, String> allQueryParams,
                                                              @RequestParam final MultipartFile schema,
                                                              @RequestParam(defaultValue = "json-schema-generated-podos") final String archiveName)
            throws IOException, GeneratorNotFoundException {
        final String fileContents = new String(schema.getBytes());
        final Map<String, String> filteredQueryParams = removeKnownQueryParameters(allQueryParams);
        return buildResponseEntity(generatorName, optionsTransformer.getCommandOptions(generatorName, filteredQueryParams), fileContents, archiveName);
    }

    /**
     * Overloaded Endpoint Handler for "/{generatorName}"
     * It generates an archive with PODOs, based on
     * input schema and options
     *
     * @param generatorName  target generator
     * @param allQueryParams custom specific generator options
     * @param schema         the input JSON Schema
     * @param archiveName    name of the generated code archive
     * @return the generated code archive
     * @throws IOException
     * @throws GeneratorNotFoundException in case the generator was not found
     */
    @PostMapping(value = "/{generatorName}",
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public ResponseEntity<Resource> generateFromBodyPayload(@PathVariable final String generatorName,
                                                            @RequestParam(required = false) final Map<String, String> allQueryParams,
                                                            @RequestBody final String schema,
                                                            @RequestParam(defaultValue = "json-schema-generated-podos") final String archiveName)
            throws IOException, GeneratorNotFoundException {
        final Map<String, String> filteredQueryParams = removeKnownQueryParameters(allQueryParams);
        return buildResponseEntity(generatorName, optionsTransformer.getCommandOptions(generatorName, filteredQueryParams), schema, archiveName);
    }

    private ResponseEntity<Resource> buildResponseEntity(final String generatorName,
                                                         final String[] options,
                                                         final String schema,
                                                         final String archiveName)
            throws GeneratorNotFoundException, IOException {
        final InputStreamResource archive = service.buildCodeArchive(generatorName, options, schema);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archiveName + ".zip\"")
                .body(archive);
    }

    /**
     * Parse all passed query parameters and
     * remove "well-known" parameters
     *
     * @param allQueryParams all query parameters of the request
     * @return query parameters without "well-known" ones
     */
    private Map<String, String> removeKnownQueryParameters(final Map<String, String> allQueryParams) {
        return allQueryParams
                .entrySet()
                .stream()
                .filter(param -> !param.getKey().equals("archiveName"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
