package net.gspatace.json.schema.podo.generator.langs;

import net.gspatace.json.schema.podo.generator.annotations.CustomProperties;
import net.gspatace.json.schema.podo.generator.annotations.SchemaGenerator;
import net.gspatace.json.schema.podo.generator.base.AbstractGenerator;
import net.gspatace.json.schema.podo.generator.base.BaseOptions;
import net.gspatace.json.schema.podo.generator.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.templating.TemplateFile;
import picocli.CommandLine;

import java.util.Optional;

@SchemaGenerator(name = "java", embeddedResourceLocation = "java",
        description = "Just for tests.")
public class JavaGenerator extends AbstractGenerator {

    private final JavaOptionsCli javaSpecificProperties = new JavaOptionsCli();

    public JavaGenerator(BaseOptions baseCliOptions) {
        super(baseCliOptions);
        //addTemplateFile(new TemplateFile("source.mustache", "java"));
        final String subDir = "src/main/java";
        addTemplateFile(TemplateFile.builder()
                .templateName("source.mustache")
                .fileExtension("java")
                .subDirectory(Optional.of(subDir)).build());

        addBaseDataTypeMapping(JsonDataTypes.INTEGER, "int");
        addBaseDataTypeMapping(JsonDataTypes.NUMBER, "double");
        addBaseDataTypeMapping(JsonDataTypes.BOOLEAN, "boolean");
        addBaseDataTypeMapping(JsonDataTypes.STRING, "String");
        addBaseDataTypeMapping(JsonDataTypes.ARRAY, "ArrayList");

        parseCustomOptionsProperties(javaSpecificProperties);
    }

    @CustomProperties
    public static class JavaOptionsCli {
        @CommandLine.Option(names = "package")
        private String apiPackage;
    }
}
