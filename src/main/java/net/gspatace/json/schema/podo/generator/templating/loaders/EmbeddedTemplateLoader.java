package net.gspatace.json.schema.podo.generator.templating.loaders;

import net.gspatace.json.schema.podo.generator.templating.interfaces.TemplateLoader;

import java.io.InputStream;

public class EmbeddedTemplateLoader implements TemplateLoader {

    private final String resourceSubdirectory;

    public EmbeddedTemplateLoader(final String resourceSubdirectory) {
        this.resourceSubdirectory = resourceSubdirectory;
    }

    @Override
    public InputStream getTemplateInputStream(final String templateName) {
        final String templatePath = String.format("%s/%s", resourceSubdirectory, templateName);
        return this.getClass().getClassLoader().getResourceAsStream(templatePath);
    }
}
