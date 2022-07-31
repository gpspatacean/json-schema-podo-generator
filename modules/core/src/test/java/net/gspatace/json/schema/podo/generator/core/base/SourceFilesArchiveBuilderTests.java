package net.gspatace.json.schema.podo.generator.core.base;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * @author George Spătăcean
 */
public class SourceFilesArchiveBuilderTests {

    /**
     * Archive list of {@link ProcessedSourceFile} and
     * check if the output matches the input
     * 
     * @throws IOException
     */
    @Test
    public void checkArchiveBuilder() throws IOException {
        final List<ProcessedSourceFile> files = new ArrayList<>();
        files.add(new ProcessedSourceFile("foo.txt", "foo_txt_contents.txt"));
        files.add(new ProcessedSourceFile("foo/bar.txt", "foo_bar_txt_contents.txt"));
        files.add(new ProcessedSourceFile("foo/bar/baz.txt", "foo_bar_baz_txt_contents.txt"));
        files.add(new ProcessedSourceFile("foo/bar/qux.txt", "foo_bar_qux_txt\ncontents.txt"));

        final byte[] zippedBytes = new SourceFilesArchiveBuilder(files).buildArchive();
        final SeekableInMemoryByteChannel bytesChannel = new SeekableInMemoryByteChannel(zippedBytes);
        final ZipFile theFile = new ZipFile(bytesChannel);
        final Enumeration<ZipArchiveEntry> entries = theFile.getEntries();

        while (entries.hasMoreElements()) {
            final ZipArchiveEntry entry = entries.nextElement();
            final byte[] entryContents = new byte[(int) entry.getSize()];
            int bytesRead = 0;
            while (bytesRead < entryContents.length) {
                bytesRead += theFile.getInputStream(entry).read(entryContents, bytesRead, entryContents.length - bytesRead);
            }
            final ProcessedSourceFile rebuiltProcessedFile = new ProcessedSourceFile(entry.getName(), new String(entryContents));
            assertTrue("Rebuilt Processed File matches an initial one", files.contains(rebuiltProcessedFile));
        }
    }
}
