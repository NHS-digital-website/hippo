package uk.nhs.digital.ps.test.acceptance.util;

import static java.nio.file.Files.readAllBytes;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import uk.nhs.digital.ps.test.acceptance.models.FileType;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHelper {

    public static Path createFile(final Path location, final String name, final byte[] content) {
        try {
            Files.createDirectories(location);

            final Path tempFile = Files.createFile(Paths.get(location.toString(), name));

            Files.write(tempFile, content);

            return tempFile;

        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    public static byte[] readFileAsByteArray(final Path path) {
        try {
            return readAllBytes(path);
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    /**
     * Blocks current thread until the given file appears.
     * Throws an exception if the file doesn't appear within 10 seconds.
     */
    public static void waitUntilFileAppears(final Path path) {
        FileUtils.waitFor(path.toFile(), 10);
        if (Files.notExists(path)) {
            throw new RuntimeException("File did not appear within the defined timeout: " + path.toString());
        }
    }

    public static FileType getFileType(final Path path) {
        return FileType.valueOf(FilenameUtils.getExtension(path.getFileName().toString().toUpperCase()));
    }

    public static String toHumanFriendlyFileSize(final long bytesCount) {

        // follows uk.nhs.digital.ps.directives.FileSizeFormatterDirective

        String formattedFileSize = "";

        final int unit = 1000;
        if (bytesCount < unit) {
            formattedFileSize = bytesCount + " B";
        } else {
            final int exp = (int) (Math.log(bytesCount) / Math.log(unit));
            final String pre = String.valueOf(("kMGTPE").charAt(exp - 1));
            formattedFileSize = String.format("%.1f %sB", bytesCount / Math.pow(unit, exp), pre);
        }

        return formattedFileSize;
    }
}
