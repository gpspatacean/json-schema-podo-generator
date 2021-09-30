package net.gspatace.json.schema.podo.generator.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.annotations.SchemaGenerator;
import net.gspatace.json.schema.podo.generator.generators.JsonSchemaGenData;
import net.gspatace.json.schema.podo.generator.generators.JsonSchemaParser;
import net.gspatace.json.schema.podo.generator.specification.models.JsonSchema;
import net.gspatace.json.schema.podo.generator.templating.TemplateManager;
import net.gspatace.json.schema.podo.generator.templating.interfaces.TemplateLoader;
import net.gspatace.json.schema.podo.generator.templating.loaders.EmbeddedTemplateLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static net.gspatace.json.schema.podo.generator.utils.ObjectMapperFactory.createDefaultObjectMapper;

/**
 * Main base class for all generators
 * It keeps common logic and the drives the main code generation workflow
 *
 * @author George Spătăcean
 */
@Slf4j
public abstract class AbstractGenerator {
    private final String schemaInput;
    private final ProcessedTemplatesWriter writer;
    private final List<String> templateList = new ArrayList<>();
    private final SchemaGenerator generatorAnnotation;

    /**
     * Protected constructor
     *
     * @param baseCliOptions the command line options as passed to the
     *                       main generate command in {@link net.gspatace.json.schema.podo.generator.cli.commands.GenerateCommand#run()}
     */
    protected AbstractGenerator(BaseOptions baseCliOptions) {
        this.schemaInput = baseCliOptions.getInputSpec();
        //TODO <gspatace> don't really know if this is smart
        // ( read/use derived class annotation in base constructor )
        generatorAnnotation = this.getClass().getAnnotation(SchemaGenerator.class);
        writer = new ProcessedTemplatesWriter(baseCliOptions.getOutputDirectory());
    }

    /**
     * Retrieves a JSON Schema Generator Data POJO for the passed schema
     *
     * @return POJO representation of the passed schema
     * @throws JsonProcessingException if error occurred while transforming
     *                                 input schema.
     */
    protected JsonSchemaGenData getJsonSchemaGenData() throws JsonProcessingException {
        final ObjectMapper objectMapper = createDefaultObjectMapper();
        final String schema = getSchema();
        final JsonSchema jsonSchema = objectMapper.readValue(schema, JsonSchema.class);
        return JsonSchemaParser.getGeneratorData(jsonSchema);
    }

    /**
     * Parses and retrieves the provided Schema passed to the generator
     *
     * @return Schema contents as string
     */
    protected String getSchema() {
        final Path schemePath = FileSystems.getDefault().getPath(schemaInput);
        try (InputStream iss = new FileInputStream(schemePath.toFile());
             InputStreamReader inputStreamReader = new InputStreamReader(iss, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Template registration of concrete generator implementations
     *
     * @param templateName name of template to be executed
     */
    protected void addTemplateFile(final String templateName) {
        templateList.add(templateName);
    }

    /**
     * Main generator driver method. It follows this workflow:
     * <ol>
     *     <li>Template management preparation</li>
     *     <li>Templates execution</li>
     *     <li>Final write of executed templates</li>
     * </ol>
     *
     * @throws JsonProcessingException if errors occured while processing the provided JSON Schema
     */
    public void generate() throws JsonProcessingException {
        final TemplateLoader templateLoader = new EmbeddedTemplateLoader(embeddedResourceLocation());
        final TemplateManager templateManager = new TemplateManager(templateLoader);
        final JsonSchemaGenData schemaData = getJsonSchemaGenData();
        templateList.forEach(template -> {
            final String resolvedTemplate = templateManager.executeTemplate(template, schemaData);
            writer.addProcessedTemplate(template, resolvedTemplate);
            log.debug("Added resolved template {}", template);
        });
        writer.writeToDisk();
    }

    /**
     * Accessor of the name
     * for the current running concrete generator
     *
     * @return name
     */
    public String name() {
        return generatorAnnotation.name();
    }

    /**
     * Accessor of the embedded location of the templates
     * for the current running concrete generator
     *
     * @return resource location
     */
    public String embeddedResourceLocation() {
        final String location = generatorAnnotation.embeddedResourceLocation();
        log.trace("Generator `{}` is loading templates from `{}` resources directory.", name(), location);
        return location;
    }
}
