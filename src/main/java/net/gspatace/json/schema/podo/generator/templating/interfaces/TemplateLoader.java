package net.gspatace.json.schema.podo.generator.templating.interfaces;

import net.gspatace.json.schema.podo.generator.templating.exceptions.TemplateNotLoadedException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Loader interface for templates.
 *
 * @author George Spătăcean
 */
public interface TemplateLoader {
    /**
     * Get a handler to a stream from where contents of the template
     * can be retrieved.
     * @param templateName name of the target template
     * @return {@link InputStream} to the template
     * @throws FileNotFoundException in case the template cannot be found
     */
    InputStream getTemplateInputStream(final String templateName) throws FileNotFoundException;

    /**
     * Returns the contents of a given template. Internally uses the
     * overridden version of {@link #getTemplateInputStream(String)} to
     * retrieve a stream to the template.
     *
     * @param templateName the target template
     * @return the contents of the template
     * @throws TemplateNotLoadedException if the template cannot be loaded.
     */
    default String loadTemplate(final String templateName) {
        try (InputStream iss = getTemplateInputStream(templateName);
             InputStreamReader inputStreamReader = new InputStreamReader(iss, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            System.err.println("Yes, Rico, Ka-boom!");
            throw new TemplateNotLoadedException("Failed to load Template: ", e);
        }
    }
}
