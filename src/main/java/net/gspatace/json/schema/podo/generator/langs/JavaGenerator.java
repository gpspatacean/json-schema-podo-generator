package net.gspatace.json.schema.podo.generator.langs;

import net.gspatace.json.schema.podo.generator.annotations.SchemaGenerator;
import net.gspatace.json.schema.podo.generator.base.AbstractGenerator;
import net.gspatace.json.schema.podo.generator.base.BaseOptions;
import picocli.CommandLine;

@SchemaGenerator(name = "Java Generator", embeddedResourceLocation = "cpp",
                    description = "Just for tests.")
public class JavaGenerator extends AbstractGenerator {
    protected JavaGenerator(BaseOptions baseCliOptions) {
        super(baseCliOptions);
        addTemplateFile("header.mustache");
    }

    public static class JavaOptionsCli {
        @CommandLine.Option(names = "package")
        private String apiPackage;
    }
}
