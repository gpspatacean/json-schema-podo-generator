package com.spatacean.json.schema.podo.generator.core.langs;

import com.spatacean.json.schema.podo.generator.core.generators.JsonSchemaGenData;
import com.spatacean.json.schema.podo.generator.core.templating.SupportFile;
import com.spatacean.json.schema.podo.generator.core.templating.TemplateFile;
import com.spatacean.json.schema.podo.generator.core.annotations.CustomProperties;
import com.spatacean.json.schema.podo.generator.core.annotations.SchemaGenerator;
import com.spatacean.json.schema.podo.generator.core.base.AbstractGenerator;
import com.spatacean.json.schema.podo.generator.core.base.GeneratorInput;
import com.spatacean.json.schema.podo.generator.core.specification.JsonDataTypes;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;

@SchemaGenerator(name = "cpp", embeddedResourceLocation = "cpp",
        description = "Generate C++ PODOs")
public class DefaultCppGenerator extends AbstractGenerator {

    private final CppSpecificProperties cppSpecificProperties = new CppSpecificProperties();

    public DefaultCppGenerator(GeneratorInput generatorInput) {
        super(generatorInput);

        addTemplateFile(TemplateFile.builder().templateName("header.mustache").fileExtension("hpp").build());
        addTemplateFile(TemplateFile.builder().templateName("source.mustache").fileExtension("cpp").build());

        addSupportFile(new SupportFile("modelbase-header.mustache", "ModelBase.hpp"));
        addSupportFile(new SupportFile("modelbase-source.mustache", "ModelBase.cpp"));
        addSupportFile(new SupportFile("CMakeLists.mustache", "CMakeLists.txt"));

        addBaseDataTypeMapping(JsonDataTypes.INTEGER, "int");
        addBaseDataTypeMapping(JsonDataTypes.NUMBER, "double");
        addBaseDataTypeMapping(JsonDataTypes.BOOLEAN, "bool");
        addBaseDataTypeMapping(JsonDataTypes.STRING, "std::string");
        addBaseDataTypeMapping(JsonDataTypes.ARRAY, "std::vector");

        addLanguagePrimitive("int");
        addLanguagePrimitive("double");
        addLanguagePrimitive("bool");

        parseCustomOptionsProperties(cppSpecificProperties);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String formatModelDependency(final String dep) {
        return String.format("#include \"%s.hpp\"", dep);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fillAdditionalProperties(JsonSchemaGenData schemaGenData) {
        Map<String, Object> mainProperties = new HashMap<>();
        mainProperties.put("libraryName", cppSpecificProperties.libName);
        schemaGenData.setAdditionalProperties(mainProperties);

        Map<String, Object> modelProperties = new HashMap<>();
        modelProperties.put("namespace", cppSpecificProperties.namespace);
        schemaGenData.getModels().forEach(modelData -> modelData.setAdditionalProperties(modelProperties));
    }

    @CustomProperties
    public static class CppSpecificProperties {
        @CommandLine.Option(names = "-ns", defaultValue = "podo_generator", description = "Namespace of the generated PODOs. Defaults to \"${DEFAULT-VALUE}\"")
        private String namespace;

        @CommandLine.Option(names = "-l", defaultValue = "generatedPodos", description = "Name of the module. Defaults to \"${DEFAULT-VALUE}\"")
        private String libName;
    }
}
