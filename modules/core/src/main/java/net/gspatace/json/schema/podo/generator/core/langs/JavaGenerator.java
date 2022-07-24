package net.gspatace.json.schema.podo.generator.core.langs;

import net.gspatace.json.schema.podo.generator.core.annotations.CustomProperties;
import net.gspatace.json.schema.podo.generator.core.annotations.SchemaGenerator;
import net.gspatace.json.schema.podo.generator.core.base.AbstractGenerator;
import net.gspatace.json.schema.podo.generator.core.base.BaseOptions;
import net.gspatace.json.schema.podo.generator.core.specification.JsonDataTypes;
import net.gspatace.json.schema.podo.generator.core.templating.SupportFile;
import net.gspatace.json.schema.podo.generator.core.templating.TemplateFile;
import net.gspatace.json.schema.podo.generator.core.generators.JsonSchemaGenData;
import picocli.CommandLine;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SchemaGenerator(name = "java", embeddedResourceLocation = "java",
        description = "Java POJOs.")
public class JavaGenerator extends AbstractGenerator {

    private final JavaOptionsCli javaSpecificProperties = new JavaOptionsCli();

    public JavaGenerator(BaseOptions baseCliOptions) {
        super(baseCliOptions);

        parseCustomOptionsProperties(javaSpecificProperties);
        final String subDir = finalDirectoryDestination();
        addTemplateFile(TemplateFile.builder()
                .templateName("source.mustache")
                .fileExtension("java")
                .subDirectory(Optional.of(subDir)).build());

        addSupportFile(new SupportFile("pom.mustache", "pom.xml"));

        addBaseDataTypeMapping(JsonDataTypes.INTEGER, "int");
        addBaseDataTypeMapping(JsonDataTypes.NUMBER, "double");
        addBaseDataTypeMapping(JsonDataTypes.BOOLEAN, "boolean");
        addBaseDataTypeMapping(JsonDataTypes.STRING, "String");
        addBaseDataTypeMapping(JsonDataTypes.ARRAY, "ArrayList");
    }

    private String packageNameToDirectoryPath() {
        return javaSpecificProperties.apiPackage.replace(".", "/");
    }

    private String finalDirectoryDestination() {
        final String subDir = "src/main/java";
        return String.format("%s/%s", subDir, packageNameToDirectoryPath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void fillAdditionalProperties(JsonSchemaGenData schemaGenData) {
        Map<String, Object> mainProperties = new HashMap<>();
        mainProperties.put("artifactId", javaSpecificProperties.artifactId);
        mainProperties.put("groupId", javaSpecificProperties.groupId);
        mainProperties.put("version", javaSpecificProperties.version);
        schemaGenData.setAdditionalProperties(mainProperties);

        Map<String, Object> modelProperties = new HashMap<>();
        modelProperties.put("apiPackage", javaSpecificProperties.apiPackage);
        schemaGenData.getModels().forEach(modelData -> modelData.setAdditionalProperties(modelProperties));
    }

    @CustomProperties
    public static class JavaOptionsCli {
        @CommandLine.Option(names = {"-p", "--package"}, defaultValue = "generated.pojos", description = "Package containing the generated POJOs. Defaults to \"${DEFAULT-VALUE}\"")
        private String apiPackage;

        @CommandLine.Option(names = {"-a", "--artifactId"}, defaultValue = "schema-apis", description = "Artifact of the generated PODOs. Defaults to \"${DEFAULT-VALUE}\"")
        private String artifactId;

        @CommandLine.Option(names = {"-gr", "--groupId"}, defaultValue = "org.podo.generated", description = "GroupId of the generated artifact. Defaults to \"${DEFAULT-VALUE}\"")
        private String groupId;

        @CommandLine.Option(names = {"-v", "--version"}, defaultValue = "0.0.1-SNAPSHOT", description = "Version of the generated artifact. Defaults to \"${DEFAULT-VALUE}\"")
        private String version;
    }
}
