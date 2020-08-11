package uk.nhs.digital.test.util;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {

    public static String fileContentFromClasspath(final String fileClasspathPath) {

        if (FileUtils.class.getResource(fileClasspathPath) == null) {
            throw new RuntimeException("Failed to read test file as it was not found at: " + fileClasspathPath);
        }

        try (final InputStream resourceAsStream = FileUtils.class.getResourceAsStream(fileClasspathPath)) {

            return StreamUtils.copyToString(
                resourceAsStream, StandardCharsets.UTF_8
            );

        } catch (final Exception e) {
            throw new RuntimeException("Failed to read test content file from " + fileClasspathPath, e);
        }
    }

    public static String fileContentFromFilesystem(final String fileSystemPath) {

        if (Files.notExists(Paths.get(fileSystemPath))) {
            throw new RuntimeException("Failed to read test file as it was not found at: " + fileSystemPath);
        }

        try {
            return org.apache.commons.io.FileUtils.readFileToString(
                Paths.get(fileSystemPath).toFile(), StandardCharsets.UTF_8
            );
        } catch (final IOException e) {
            throw new UncheckedIOException("Failed to read test content file from " + fileSystemPath, e);
        }
    }
}
