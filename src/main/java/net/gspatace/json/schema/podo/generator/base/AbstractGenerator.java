package net.gspatace.json.schema.podo.generator.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

public abstract class AbstractGenerator {
    private final String schemaInput;
    private final ProcessedTemplatesWriter writer;
    private final List<String> templateList = new ArrayList<>();
    private final SchemaGenerator generatorAnnotation;

    protected AbstractGenerator(BaseOptions baseCliOptions) {
        this.schemaInput = baseCliOptions.getInputSpec();
        //TODO <gspatace> don't really know if this is smart
        // ( read/use derived class annotation in base constructor )
        generatorAnnotation = this.getClass().getAnnotation(SchemaGenerator.class);
        writer = new ProcessedTemplatesWriter(baseCliOptions.getOutputDirectory());
    }

    protected JsonSchemaGenData getJsonSchemaGenData() throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);

        final String schema = getSchema();
        final JsonSchema jsonSchema = objectMapper.readValue(schema, JsonSchema.class);
        return JsonSchemaParser.getGeneratorData(jsonSchema);
    }

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

    protected void addTemplateFile(final String templateName) {
        templateList.add(templateName);
    }

    public void generate() throws JsonProcessingException {
        final TemplateLoader templateLoader = new EmbeddedTemplateLoader(embeddedResourceLocation());
        final TemplateManager templateManager = new TemplateManager(templateLoader);
        final JsonSchemaGenData schemaData = getJsonSchemaGenData();
        templateList.forEach(template -> {
            final String resolvedTemplate = templateManager.executeTemplate(template, schemaData);
            writer.addProcessedTemplate(template, resolvedTemplate);
        });
        writer.writeToDisk();
    }

    public String name() {
        return generatorAnnotation.name();
    }

    public String embeddedResourceLocation() {
        return generatorAnnotation.embeddedResourceLocation();
    }
}
