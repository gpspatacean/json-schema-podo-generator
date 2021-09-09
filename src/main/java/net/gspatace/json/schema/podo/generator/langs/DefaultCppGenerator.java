package net.gspatace.json.schema.podo.generator.langs;

import net.gspatace.json.schema.podo.generator.annotations.CustomProperties;
import net.gspatace.json.schema.podo.generator.annotations.SchemaGenerator;
import net.gspatace.json.schema.podo.generator.base.AbstractGenerator;
import net.gspatace.json.schema.podo.generator.base.BaseOptions;
import picocli.CommandLine;

@SchemaGenerator(name = "cpp", embeddedResourceLocation = "cpp",
        description = "Generate C++ PODOs")
public class DefaultCppGenerator extends AbstractGenerator {

    private final CppSpecificProperties cppSpecificProperties = new CppSpecificProperties();

    public DefaultCppGenerator(BaseOptions baseOptions) {
        super(baseOptions);

        addTemplateFile("header.mustache");
        addTemplateFile("source.mustache");
    }

    @CustomProperties
    public static class CppSpecificProperties {
        @CommandLine.Option(names = "-ns", description = "Namespace of the generated PODOs")
        private String namespace;

        @CommandLine.Option(names = "-l", description = "Name of the module")
        private String libName;
    }
}
