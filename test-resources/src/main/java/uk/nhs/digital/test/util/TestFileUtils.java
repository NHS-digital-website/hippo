package uk.nhs.digital.test.util;

import static org.apache.commons.io.FileUtils.readFileToString;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestFileUtils {

    public static String contentOfFileFromClasspath(final String fileClasspathPath) {

        final URL resource = TestFileUtils.class.getResource(fileClasspathPath);

        if (resource == null) {
            throw new TestFileAccessException("Failed to read test data file as it was not found at: " + fileClasspathPath);
        }

        try {
            final File file = new File(resource.toURI());

            return readFileToString(
                file, StandardCharsets.UTF_8
            );

        } catch (final Exception e) {
            throw new TestFileAccessException("Failed to read test content file from " + fileClasspathPath, e);
        }
    }

    public static String fileContentFromFilesystem(final String fileSystemPath) {

        if (Files.notExists(Paths.get(fileSystemPath))) {
            throw new TestFileAccessException("Failed to read test data file as it was not found at: " + fileSystemPath);
        }

        try {
            return readFileToString(
                Paths.get(fileSystemPath).toFile(), StandardCharsets.UTF_8
            );
        } catch (final Exception e) {
            throw new TestFileAccessException("Failed to read test content file from " + fileSystemPath, e);
        }
    }

    public static void deleteFileOrDirectoryRecursivelyQuietly(final Path path) {
        deleteFileOrDirectoryRecursivelyQuietly(path.toFile());
    }

    public static void deleteFileOrDirectoryRecursivelyQuietly(final File file) {
        FileUtils.deleteQuietly(file);
    }

    public static class TestFileAccessException extends RuntimeException {

        public TestFileAccessException(final String message) {
            super(message);
        }

        public TestFileAccessException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }
}
