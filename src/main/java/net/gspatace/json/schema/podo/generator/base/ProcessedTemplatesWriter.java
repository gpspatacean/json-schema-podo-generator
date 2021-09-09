package net.gspatace.json.schema.podo.generator.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProcessedTemplatesWriter {
    private final Map<String, String> processedTemplates = new ConcurrentHashMap<>();
    private final Path outputDirectory;

    public ProcessedTemplatesWriter(final String outputDir) {
        final Path temporaryPath = Paths.get(outputDir);
        if (temporaryPath.isAbsolute()) {
            outputDirectory = temporaryPath;
        } else {
            final Path currentDir = Paths.get("").toAbsolutePath();
            outputDirectory = currentDir.resolve(outputDir);
        }
    }

    public void addProcessedTemplate(final String name, final String content) {
        processedTemplates.put(name, content);
    }

    public void writeToDisk() {
        processedTemplates.forEach((name, contents) -> {
            final File target = outputDirectory.resolve(name).toFile();
            try {
                Files.createDirectories(Paths.get(target.getParent()));
                final FileWriter fileWriter = new FileWriter(target);
                fileWriter.write(contents);
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
