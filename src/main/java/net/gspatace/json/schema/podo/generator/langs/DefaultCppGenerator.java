package net.gspatace.json.schema.podo.generator.langs;

import net.gspatace.json.schema.podo.generator.annotations.CustomProperties;
import net.gspatace.json.schema.podo.generator.annotations.SchemaGenerator;
import net.gspatace.json.schema.podo.generator.base.AbstractGenerator;
import net.gspatace.json.schema.podo.generator.base.BaseOptions;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.templating.TemplateFile;
import picocli.CommandLine;

@SchemaGenerator(name = "cpp", embeddedResourceLocation = "cpp",
        description = "Generate C++ PODOs")
public class DefaultCppGenerator extends AbstractGenerator {

    private final CppSpecificProperties cppSpecificProperties = new CppSpecificProperties();

    public DefaultCppGenerator(BaseOptions baseOptions) {
        super(baseOptions);

        addTemplateFile(new TemplateFile("header.mustache", "hpp"));
        addTemplateFile(new TemplateFile("source.mustache", "cpp"));

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

    @CustomProperties
    public static class CppSpecificProperties {
        @CommandLine.Option(names = "-ns", description = "Namespace of the generated PODOs")
        private String namespace;

        @CommandLine.Option(names = "-l", description = "Name of the module")
        private String libName;
    }
}
