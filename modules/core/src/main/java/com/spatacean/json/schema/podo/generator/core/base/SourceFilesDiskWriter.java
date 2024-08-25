package com.spatacean.json.schema.podo.generator.core.base;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Writer of processed source files.
 */
@Slf4j
public class SourceFilesDiskWriter {
    private final List<ProcessedSourceFile> fileList;
    private final Path outputDirectory;

    /**
     * Public constructor
     *
     * @param processedSources list of final processed files
     * @param outputDir either full or relative path where the output
     */
    public SourceFilesDiskWriter(final List<ProcessedSourceFile> processedSources, final String outputDir) {
        fileList = processedSources;
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
     * Write in the already specified directory all the files
     */
    public void writeToDisk() {
        fileList.forEach(processedSourceFile -> {
            final File target = outputDirectory.resolve(processedSourceFile.filePath()).toFile();
            try {
                log.info("Writing file `{}`", target);
                Files.createDirectories(Paths.get(target.getParent()));
                final FileWriter fileWriter = new FileWriter(target);
                fileWriter.write(processedSourceFile.fileContent());
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                log.error("Failed to write file `{}`", target, e);
            }
        });
    }
}
