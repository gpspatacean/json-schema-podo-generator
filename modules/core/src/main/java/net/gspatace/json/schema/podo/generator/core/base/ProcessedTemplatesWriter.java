package net.gspatace.json.schema.podo.generator.core.base;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holder and writer of already processed templates.
 * This will write all the templates as final part of the
 * code generation workflow, as implemented in {@link AbstractGenerator#generate()}
 */
@Slf4j
public class ProcessedTemplatesWriter {
    private final Map<String, String> processedTemplates = new ConcurrentHashMap<>();
    private final Path outputDirectory;

    /**
     * Public constructor
     *
     * @param outputDir either full or relative path where the output
     *                  of the generator should be written
     */
    public ProcessedTemplatesWriter(final String outputDir) {
        final Path temporaryPath = Paths.get(outputDir);
        if (temporaryPath.isAbsolute()) {
            outputDirectory = temporaryPath;
        } else {
            final Path currentDir = Paths.get("").toAbsolutePath();
            outputDirectory = currentDir.resolve(outputDir);
        }
        log.trace("Generated code will be written to `{}`", outputDirectory);
    }

    /**
     * Add to the processed templates list a new file to be written
     *
     * @param name    name of the file that must be created
     * @param content contents to be written, after Mustache execution
     *                has been completed.
     */
    public void addProcessedTemplate(final String name, final String content) {
        processedTemplates.put(name, content);
    }

    /**
     * Write in the already specified directory all the files
     */
    public void writeToDisk() {
        processedTemplates.forEach((name, contents) -> {
            final File target = outputDirectory.resolve(name).toFile();
            try {
                log.info("Writing file `{}`", target);
                Files.createDirectories(Paths.get(target.getParent()));
                final FileWriter fileWriter = new FileWriter(target);
                fileWriter.write(contents);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                log.error("Failed to write file `{}`", target, e);
            }
        });
    }
}
