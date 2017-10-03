package uk.nhs.digital.ps.test.acceptance.util;

import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.Files.readAllBytes;

public class FileHelper {

    public static Path createFile(final Path location, final String name, final byte[] content) {
        try {
            Files.createDirectories(location);

            final Path tempFile = Files.createFile(Paths.get(location.toString(), name));

            Files.write(tempFile, content);

            return tempFile;

        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static byte[] readFileAsByteArray(final Path path) {
        try {
            return readAllBytes(path);
        } catch (final IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Blocks current thread until the given file appears.
     * Throws an exception if the file doesn't appear within 5 seconds.
     */
    public static void waitUntilFileAppears(final Path path) {
        FileUtils.waitFor(path.toFile(), 5);
        if (Files.notExists(path)) {
            throw new RuntimeException("File did not appear within the defined timeout: " + path.toString());
        }
    }
}
