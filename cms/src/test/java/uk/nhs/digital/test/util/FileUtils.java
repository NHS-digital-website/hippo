package uk.nhs.digital.test.util;

import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    public static String contentOfFileFromClasspath(final String fileClasspathPath) {

        if (FileUtils.class.getResource(fileClasspathPath) == null) {
            throw new TestFileAccessException("Failed to read test data file as it was not found at: " + fileClasspathPath);
        }

        try (final InputStream resourceAsStream = FileUtils.class.getResourceAsStream(fileClasspathPath)) {

            return StreamUtils.copyToString(
                resourceAsStream, StandardCharsets.UTF_8
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
            return org.apache.commons.io.FileUtils.readFileToString(
                Paths.get(fileSystemPath).toFile(), StandardCharsets.UTF_8
            );
        } catch (final Exception e) {
            throw new TestFileAccessException("Failed to read test content file from " + fileSystemPath, e);
        }
    }

    public static void deleteFileOrEmptyDirIfExists(final Path path) {
        try {
            Files.deleteIfExists(path);
        } catch (final Exception e) {
            throw new TestFileAccessException("Failed to delete " + path, e);
        }
    }

    public static void deleteFileOrEmptyDirIfExists(final File file) {
        deleteFileOrEmptyDirIfExists(file.toPath());
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
