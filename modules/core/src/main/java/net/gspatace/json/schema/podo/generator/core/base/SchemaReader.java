package net.gspatace.json.schema.podo.generator.core.base;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.stream.Collectors;

/**
 * Utility class for reading a JSON Schema
 *
 * @author George Spătăcean
 */
public class SchemaReader {

    private SchemaReader() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Read and return contents of a JSON Schema File
     *
     * @param schemaFileName path to the file containg the schema
     * @return Schema contents as string
     */
    public static String getSchema(final String schemaFileName) {
        final Path schemePath = FileSystems.getDefault().getPath(schemaFileName);
        try (InputStream iss = new FileInputStream(schemePath.toFile());
             InputStreamReader inputStreamReader = new InputStreamReader(iss, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return bufferedReader.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            throw new SchemaRetrievalException("Failed to get input schema", e);
        }
    }
}
