package net.gspatace.json.schema.podo.generator.core.base;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Build an archive with all the source files provided
 *
 * @author George Spătăcean
 */
@AllArgsConstructor
@Slf4j
public class SourceFilesArchiveBuilder {
    /**
     * List of {@link ProcessedSourceFile} that will be archived
     */
    private final List<ProcessedSourceFile> fileList;

    /**
     * Build a ZIP archive and return it as byte array
     *
     * @return byte[] representing content of the archive
     * @throws IOException in case of byte stream errors
     */
    public byte[] buildArchive() throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(byteArrayOutputStream);

        for (final ProcessedSourceFile processedSourceFile : fileList) {
            try {
                final ZipArchiveEntry zipEntry = new ZipArchiveEntry(processedSourceFile.getFilePath());
                final byte[] bytesToWrite = processedSourceFile.getFileContent().getBytes(StandardCharsets.UTF_8);
                zipEntry.setSize(bytesToWrite.length);
                zipOutputStream.putArchiveEntry(zipEntry);
                zipOutputStream.write(bytesToWrite);
                zipOutputStream.closeArchiveEntry();
            } catch (IOException exception) {
                log.error("Failed to archive file {}", processedSourceFile.getFilePath(), exception);
            }
        }

        zipOutputStream.close();
        byteArrayOutputStream.close();

        return byteArrayOutputStream.toByteArray();
    }
}
