package com.spatacean.json.schema.podo.generator.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author George Spătăcean
 */
public class Utils {
    public static Optional<String> getSchemaFromResource(final ClassLoader classLoader, final String jsonInputFile) {
        try (final InputStream iss = classLoader.getResourceAsStream(jsonInputFile);
             final InputStreamReader inputStreamReader = new InputStreamReader(iss, StandardCharsets.UTF_8);
             final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            final String result = bufferedReader.lines().collect(Collectors.joining("\n"));
            return Optional.of(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
