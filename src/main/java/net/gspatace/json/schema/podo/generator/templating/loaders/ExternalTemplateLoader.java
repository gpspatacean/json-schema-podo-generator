package net.gspatace.json.schema.podo.generator.templating.loaders;

import net.gspatace.json.schema.podo.generator.templating.interfaces.TemplateLoader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class ExternalTemplateLoader implements TemplateLoader {

    private final Path directory;

    public ExternalTemplateLoader(final String directoryPath) {
        directory = FileSystems.getDefault().getPath(directoryPath);
    }

    @Override
    public InputStream getTemplateInputStream(final String templateName) throws FileNotFoundException {
        final Path fullTemplatePath = directory.resolve(templateName);
        return new FileInputStream(fullTemplatePath.toFile());
    }
}
