package net.gspatace.json.schema.podo.generator.templating;

import lombok.Builder;
import lombok.Getter;

import java.util.Optional;

/**
 * Represents a template that is being registered and will be
 * executed against the built context for a given generator.
 *
 * @author George Spătăcean
 */

@Builder
@Getter
public class TemplateFile {
    /**
     * Name of the template, as it is found in the template directory
     */
    private String templateName;

    /**
     * File extension, used for cases when the same model has 2 files
     * e.g C++ hpp/cpp
     */
    private String fileExtension;

    /**
     * Destination subdirectory, relative to the output directory.
     * For instance, {@code src/java/main/<some>/<package>}
     */
    @Builder.Default
    private Optional<String> subDirectory = Optional.empty();

    /**
     * Get full destination path
     *
     * @param fileName the file name
     * @return the final file name and location, relative to the
     * destination directory
     */
    public String getOutputFilePath(final String fileName) {
        if (subDirectory.isPresent()) {
            return subDirectory.get() + "/" + fileName + "." + fileExtension;
        }

        return fileName + "." + fileExtension;
    }
}
