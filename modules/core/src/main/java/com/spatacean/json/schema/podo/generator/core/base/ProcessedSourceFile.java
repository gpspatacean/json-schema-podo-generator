package com.spatacean.json.schema.podo.generator.core.base;

/**
 * Container of a processed source file
 *
 * @param filePath    File path including directories from the
 *                    initial logic directory.
 *                    e.g. "src/main/java/some/package/File.java" or
 *                    "Model.hpp"
 * @param fileContent Final contents of a source
 * @author George Spătăcean
 */
public record ProcessedSourceFile(String filePath, String fileContent) {
}
