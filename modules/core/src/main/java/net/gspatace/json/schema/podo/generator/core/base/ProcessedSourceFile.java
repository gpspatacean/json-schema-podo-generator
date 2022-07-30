package net.gspatace.json.schema.podo.generator.core.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author George Spătăcean
 */

@AllArgsConstructor
@Getter
public class ProcessedSourceFile {
    /**
     * File path including directories from the
     * initial logic directory.
     * e.g. "src/main/java/some/package/File.java" or
     * "Model.hpp"
     */
    private final String filePath;

    /**
     * Final contents of a source
     */
    private final String fileContent;
}
