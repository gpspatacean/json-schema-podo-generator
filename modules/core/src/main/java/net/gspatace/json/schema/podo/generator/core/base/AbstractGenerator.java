package net.gspatace.json.schema.podo.generator.core.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.gspatace.json.schema.podo.generator.core.annotations.SchemaGenerator;
import net.gspatace.json.schema.podo.generator.core.generators.JsonSchemaGenData;
import net.gspatace.json.schema.podo.generator.core.generators.JsonSchemaParser;
import net.gspatace.json.schema.podo.generator.core.generators.MemberVariableData;
import net.gspatace.json.schema.podo.generator.core.generators.ModelData;
import net.gspatace.json.schema.podo.generator.core.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.core.specification.models.JsonSchema;
import net.gspatace.json.schema.podo.generator.core.templating.SupportFile;
import net.gspatace.json.schema.podo.generator.core.templating.TemplateFile;
import net.gspatace.json.schema.podo.generator.core.templating.TemplateManager;
import net.gspatace.json.schema.podo.generator.core.templating.interfaces.TemplateLoader;
import net.gspatace.json.schema.podo.generator.core.templating.loaders.EmbeddedTemplateLoader;
import net.gspatace.json.schema.podo.generator.core.utils.ObjectMapperFactory;
import org.apache.commons.lang3.StringUtils;
import picocli.CommandLine;

import java.util.*;

/**
 * Main base class for all generators
 * It keeps common logic and the drives the main code generation workflow
 *
 * @author George Spătăcean
 */
@Slf4j
public abstract class AbstractGenerator {
    /**
     * Holder for JSON Schema type to Language specific type mappings
     * For instance, {@code STRING -> std::string; BOOLEAN -> bool; } for C++
     */
    protected final Map<JsonDataTypes, String> baseDataTypesMappings = new EnumMap<>(JsonDataTypes.class);

    /**
     * JSON Schema for which we want to generate PODOs
     */
    private final String inputSchema;

    /**
     * Holder for processed source files
     */
    private final List<ProcessedSourceFile> processedFiles = new ArrayList<>();

    /**
     * List of templates used for generation of each entity
     */
    private final List<TemplateFile> templateList = new ArrayList<>();

    /**
     * Supporting files, for instance CMakeLists.txt, pom.xml, READMEs, etc.
     */
    private final List<SupportFile> supportFiles = new ArrayList<>();

    /**
     * String containing command line properties specific to a certain generator
     * For instance, namespace for C++, packages for Java, etc.
     */
    private final String[] customPropertiesInput;

    /**
     * Handler to the annotation of the concrete generator instantiation
     */
    private final SchemaGenerator generatorAnnotation;

    /**
     * List of primitives specific to a concrete generator
     */
    private final List<String> languagePrimitives = new ArrayList<>();

    /**
     * Protected constructor
     *
     * @param generatorInput holder of input needed by a generator
     */
    protected AbstractGenerator(final GeneratorInput generatorInput) {
        inputSchema = generatorInput.getInputSpec();
        //TODO <gspatace> don't really know if this is smart
        // ( read/use derived class annotation in base constructor )
        generatorAnnotation = this.getClass().getAnnotation(SchemaGenerator.class);
        customPropertiesInput = generatorInput.getGeneratorSpecificProperties();
    }

    /**
     * Retrieves a JSON Schema Generator Data POJO for the passed schema
     *
     * @return POJO representation of the passed schema
     * @throws JsonProcessingException if error occurred while transforming
     *                                 input schema.
     */
    protected JsonSchemaGenData getJsonSchemaGenData() throws JsonProcessingException {
        final ObjectMapper objectMapper = ObjectMapperFactory.createDefaultObjectMapper();
        final JsonSchema jsonSchema = objectMapper.readValue(inputSchema, JsonSchema.class);
        return new JsonSchemaParser(jsonSchema).getGeneratorData();
    }

    /**
     * Model template registration of concrete generator implementations
     *
     * @param templateFile template to be executed
     */
    protected void addTemplateFile(final TemplateFile templateFile) {
        templateList.add(templateFile);
    }

    /**
     * Auxiliary file registration of concrete generator implementations
     *
     * @param supportFile template to be executed
     */
    protected void addSupportFile(final SupportFile supportFile) {
        supportFiles.add(supportFile);
    }

    /**
     * Adds a mapping from JSON Schema Datatype to Language Datatype
     *
     * @param type         JSON Schema Datatype
     * @param languageType Language Datatype
     */
    protected void addBaseDataTypeMapping(final JsonDataTypes type, final String languageType) {
        baseDataTypesMappings.put(type, languageType);
    }

    /**
     * Checks to see if a given JSON Datatype is registered as a base datatype, as per
     * Generator Configuration
     *
     * @param type JSON Type to check
     * @return true/false
     */
    protected boolean isBaseType(final JsonDataTypes type) {
        return baseDataTypesMappings.containsKey(type);
    }

    /**
     * Adds a type as a primitive to the list.
     *
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
     * @return List of processed source files to be handled
     * @throws JsonProcessingException if errors occurred while processing the provided JSON Schema
     */
    public List<ProcessedSourceFile> generate() throws JsonProcessingException {
        final TemplateLoader templateLoader = new EmbeddedTemplateLoader(embeddedResourceLocation());
        final TemplateManager templateManager = new TemplateManager(templateLoader);
        final JsonSchemaGenData schemaData = getJsonSchemaGenData();
        specializeGeneratorData(schemaData);
        fillAdditionalProperties(schemaData);
        generateModels(templateManager, schemaData.getModels());
        generateSupportFiles(templateManager, schemaData);
        return processedFiles;
    }

    /**
     * Execute auxiliary templates against the current context
     *
     * @param templateManager will be used for actual file writing
     */
    private void generateSupportFiles(final TemplateManager templateManager, final JsonSchemaGenData generatorData) {
        supportFiles.forEach(supportFile -> {
            final String content = templateManager.executeTemplate(supportFile.getTemplateName(), generatorData);
            processedFiles.add(new ProcessedSourceFile(supportFile.getFinalFileName(), content));
            log.debug("Added resolved template {}", supportFile.getFinalFileName());
        });
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
            modelData.setDependencyFormatter(this::formatModelDependency);
            modelData.getMembers().forEach(
                    memberVariableData -> fillLanguageDataType(memberVariableData, this::getCollectionDataType)
            );
        });
    }

    /**
     * Fill additional generic properties pertaining to a certain
     * generator implementation, e.g. library/namespace or artifact/package
     *
     * @param generatorData data to be enriched with additional properties
     */
    protected void fillAdditionalProperties(final JsonSchemaGenData generatorData) {
        log.debug("Not filling additional properties");
    }

    /**
     * For a given member, if it is a collection, return the language specific construct.
     * This can be C++ templates, Java/C# generics, etc.
     * <p>
     * For instance, for C++, the representation of a collection of "Product"s, could translate to
     * {@code std::vector<Product> products{};}
     *
     * @param member the property that is a collection
     * @return String containing the language specific construct.
     * <p>
     * Also, see {@link CollectionTypeBuilder#getCollectionDataType(MemberVariableData)}
     */
    protected String getCollectionDataType(final MemberVariableData member) {
        final StringBuilder sb = new StringBuilder();
        sb.append(baseDataTypesMappings.get(JsonDataTypes.ARRAY));
        final String dataType = isBaseType(member.getJsonDataTypes()) && !member.getInnerModel().isPresent() ?
                baseDataTypesMappings.get(JsonDataTypes.valueOf(member.getDataType().toUpperCase(Locale.ROOT))) :
                StringUtils.capitalize(member.getDataType());
        sb.append("<").append(dataType).append(">");
        return sb.toString();
    }

    /**
     * Fill information required by the mustache templates
     * <ul>
     *     <li>model name</li>
     *     <li>getter,setter name</li>
     *     <li>collections</li>
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
     * Format Model Dependencies - for instance
     * {@code #include "Dependency.hpp"} for C++
     * or {@code #import some.package.Dependency} for Java.
     * <p>
     * Can be overridden at concrete generator level.
     *
     * @param dep Name of the model for which include/import statement
     *            is required.
     * @return the formatted include/import/etc. statement
     */
    protected String formatModelDependency(final String dep) {
        return dep;
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
            final String outputFileName = template.getOutputFilePath(modelData.getModelName());
            processedFiles.add(new ProcessedSourceFile(outputFileName, resolvedTemplate));
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
