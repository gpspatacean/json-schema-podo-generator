package net.gspatace.json.schema.podo.generator.templating.loaders;

import net.gspatace.json.schema.podo.generator.templating.interfaces.TemplateLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * Template Loader that retrieves files from a specified location on disk
 *
 * @author George Spătăcean
 */
public class ExternalTemplateLoader implements TemplateLoader {

    private final Path directory;

    /**
     * Public constructor
     * @param directoryPath location on disk from where template files
     *                      should be retrieved from. Only full paths.
     */
    public ExternalTemplateLoader(final String directoryPath) {
        directory = FileSystems.getDefault().getPath(directoryPath);
    }

    @Override
    public InputStream getTemplateInputStream(final String templateName) throws FileNotFoundException {
        final Path fullTemplatePath = directory.resolve(templateName);
        return new FileInputStream(fullTemplatePath.toFile());
    }
}
