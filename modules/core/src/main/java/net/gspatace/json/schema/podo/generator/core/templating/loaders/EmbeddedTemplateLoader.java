package net.gspatace.json.schema.podo.generator.core.templating.loaders;

import net.gspatace.json.schema.podo.generator.core.templating.interfaces.TemplateLoader;

import java.io.InputStream;

/**
 * Template Loader that retrieves files from the embedded
 * "src/main/resource" directory.
 *
 * @author George Spătăcean
 */
public class EmbeddedTemplateLoader implements TemplateLoader {

    private final String resourceSubdirectory;

    /**
     * Public constructor
     * @param resourceSubdirectory the subdirectory of the "src/main/resource"
     *                             where templates for this generator reside
     */
    public EmbeddedTemplateLoader(final String resourceSubdirectory) {
        this.resourceSubdirectory = resourceSubdirectory;
    }

    @Override
    public InputStream getTemplateInputStream(final String templateName) {
        final String templatePath = String.format("%s/%s", resourceSubdirectory, templateName);
        return this.getClass().getClassLoader().getResourceAsStream(templatePath);
    }
}
