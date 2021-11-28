package net.gspatace.json.schema.podo.generator.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.annotations.SchemaGenerator;
import net.gspatace.json.schema.podo.generator.generators.JsonSchemaGenData;
import net.gspatace.json.schema.podo.generator.generators.JsonSchemaParser;
import net.gspatace.json.schema.podo.generator.generators.MemberVariableData;
import net.gspatace.json.schema.podo.generator.generators.ModelData;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.specification.models.JsonSchema;
import net.gspatace.json.schema.podo.generator.templating.TemplateFile;
import net.gspatace.json.schema.podo.generator.templating.TemplateManager;
import net.gspatace.json.schema.podo.generator.templating.interfaces.TemplateLoader;
import net.gspatace.json.schema.podo.generator.templating.loaders.EmbeddedTemplateLoader;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.*;
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
    protected final Map<JsonDataTypes, String> baseDataTypesMappings = new HashMap<>();
    private final String schemaInput;
    private final ProcessedTemplatesWriter writer;
    private final List<TemplateFile> templateList = new ArrayList<>();
    private final String[] customPropertiesInput;
    private final SchemaGenerator generatorAnnotation;
    private final List<String> languagePrimitives = new ArrayList<>();

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
        customPropertiesInput = baseCliOptions.getGeneratorSpecificProperties();
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
            throw new SchemaRetrievalException("Failed to get input schema", e);
        }
    }

    /**
     * Template registration of concrete generator implementations
     *
     * @param templateFile name of template to be executed
     */
    protected void addTemplateFile(final TemplateFile templateFile) {
        templateList.add(templateFile);
    }

    /**
     * Adds a mapping from JSON Schema Datatype to Language Datatype
     * @param type JSON Schema Datatype
     * @param languageType Language Datatype
     */
    protected void addBaseDataTypeMapping(final JsonDataTypes type, final String languageType) {
        baseDataTypesMappings.put(type, languageType);
    }

    /**
     * Checks to see if a given JSON Datatype is registered as a base datatype, as per
     * Generator Configuration
     * @param type JSON Type to check
     * @return true/false
     */
    protected boolean isBaseType(final JsonDataTypes type) {
        return baseDataTypesMappings.containsKey(type);
    }

    /**
     * Adds a type as a primitive to the list.
     * @param primitive will be registered as a primitive
     */
    protected void addLanguagePrimitive(final String primitive) {
        languagePrimitives.add(primitive);
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
        specializeGeneratorData(schemaData);
        generateModels(templateManager, schemaData.getModels());
        writer.writeToDisk();
    }

    /**
     * High Level API that will drive and derive the transformation
     * from an agnostic schema representation to usable, language specific
     * information.
     *
     * @param generatorData working representation of JSON Schema
     */
    protected void specializeGeneratorData(JsonSchemaGenData generatorData) {
        generatorData.getModels().forEach(modelData -> {
            modelData.setModelName(deriveModelName(modelData));
            modelData.getMembers().forEach(
                    memberVariableData -> fillLanguageDataType(memberVariableData, this::getCollectionDataType)
            );
        });
    }

    /**
     * @see net.gspatace.json.schema.podo.generator.base.CollectionTypeBuilder
     */
    protected String getCollectionDataType(final MemberVariableData member) {
        final StringBuilder sb = new StringBuilder();
        sb.append(baseDataTypesMappings.get(JsonDataTypes.ARRAY));
        final String dataType = isBaseType(member.getJsonDataTypes()) ?
                baseDataTypesMappings.get(JsonDataTypes.valueOf(member.getDataType().toUpperCase(Locale.ROOT))) :
                member.getDataType();
        sb.append("<").append(dataType).append(">");
        return sb.toString();
    }

    /**
     * Fill information required by the mustache templates
     * <ul>
     *     <ui>model name</ui>
     *     <ui>getter,setter name</ui>
     *     <ui>collections</ui>
     * </ul>
     *
     * @param memberVariableData    the property for which information is filled
     * @param collectionTypeBuilder used for collections, it will build the final
     *                              language construct
     */
    protected void fillLanguageDataType(MemberVariableData memberVariableData, CollectionTypeBuilder collectionTypeBuilder) {
        if (memberVariableData.getJsonDataTypes() == JsonDataTypes.ARRAY) {
            memberVariableData.setDataType(collectionTypeBuilder.getCollectionDataType(memberVariableData));
            memberVariableData.setPrimitive(false);
        } else if (baseDataTypesMappings.containsKey(memberVariableData.getJsonDataTypes())) {
            memberVariableData.setDataType(baseDataTypesMappings.get(memberVariableData.getJsonDataTypes()));
            memberVariableData.setPrimitive(languagePrimitives.contains(memberVariableData.getDataType()));
        } else {
            if (memberVariableData.getJsonDataTypes() == JsonDataTypes.OBJECT) {
                memberVariableData.getInnerModel().ifPresent(modelData -> memberVariableData.setDataType(deriveModelName(modelData)));
            } else {
                memberVariableData.setDataType(memberVariableData.getName());
            }
            memberVariableData.setPrimitive(false);
        }
        memberVariableData.setSetterName(deriveSetterName(memberVariableData));
        memberVariableData.setGetterName(deriveGetterName(memberVariableData));
    }

    /**
     * Returns the name of the model; will be used for file names.
     * Can be overridden.
     *
     * @param model current working model
     * @return name of the model
     */
    protected String deriveModelName(final ModelData model) {
        return StringUtils.capitalize(model.getModelName());
    }

    /**
     * Derive the name of the getter of the property. Can be overridden.
     *
     * @param member current property.
     * @return name of the getter
     */
    protected String deriveGetterName(final MemberVariableData member) {
        final StringBuilder getterNameBuilder = new StringBuilder();
        final String prefix = member.getDataType().toLowerCase(Locale.ROOT).equals("bool") ? "Is" : "Get";
        final String capitalizedPropertyName = StringUtils.capitalize(member.getName());
        getterNameBuilder.append(prefix).append(capitalizedPropertyName);
        return getterNameBuilder.toString();
    }

    /**
     * Derive the name of the setter of the property. Can be overridden.
     *
     * @param member current property.
     * @return name of the setter.
     */
    protected String deriveSetterName(final MemberVariableData member) {
        final StringBuilder setterNameBuilder = new StringBuilder();
        final String capitalizedPropertyName = StringUtils.capitalize(member.getName());
        setterNameBuilder.append("Set").append(capitalizedPropertyName);
        return setterNameBuilder.toString();
    }

    /**
     * Generates all the files for the provided models.
     *
     * @param templateManager will parse and execute needed templates.
     * @param models          list of models to be generated.
     */
    private void generateModels(TemplateManager templateManager, List<ModelData> models) {
        models.forEach(modelData -> generateFilesForModel(templateManager, modelData));
    }

    /**
     * For each registered template, will execute it against the specified model.
     *
     * @param templateManager will parse and execute required templates.
     * @param modelData       current working model
     */
    private void generateFilesForModel(final TemplateManager templateManager, final ModelData modelData) {
        templateList.forEach(template -> {
            final String resolvedTemplate = templateManager.executeTemplate(template.getTemplateName(), modelData);
            final String outputFileName = modelData.getModelName() + "." + template.getFileExtension();
            writer.addProcessedTemplate(outputFileName, resolvedTemplate);
            log.debug("Added resolved template {}", outputFileName);
        });
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

    /**
     * Parse custom properties command string and fill custom
     * generator properties object
     *
     * @param customPropertiesObj generator specific class instantiation
     */
    protected void parseCustomOptionsProperties(final Object customPropertiesObj) {
        try {
            final CommandLine customPropertiesCmd = new CommandLine(customPropertiesObj);
            customPropertiesCmd.parseArgs(customPropertiesInput);
        } catch (CommandLine.ParameterException ex) {
            log.error("Failed to parse custom generator options, for command {}:", Arrays.toString(customPropertiesInput), ex);
        }
    }
}
