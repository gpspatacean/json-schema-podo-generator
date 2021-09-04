package net.gspatace.json.schema.podo.generator.templating.interfaces;

import net.gspatace.json.schema.podo.generator.templating.exceptions.TemplateNotLoadedException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public interface TemplateLoader {
    InputStream getTemplateInputStream(final String templateName) throws FileNotFoundException;

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
