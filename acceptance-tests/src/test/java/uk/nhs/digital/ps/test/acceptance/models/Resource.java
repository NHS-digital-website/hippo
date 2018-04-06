package uk.nhs.digital.ps.test.acceptance.models;

import java.nio.file.Path;

public class Resource {

    private String name;
    private Path path;

    Resource(final ResourceBuilder builder) {
        name = builder.getName();
        path = builder.getPath();
    }

    /**
     * @return Full name of the file, including extension if present.
     */
    public String getFullName() {
        return name;
    }

    /**
     * @return Path to the actual file backing this attachment: {@code null} if there is no such file.
     */
    public Path getPath() {
        return path;
    }
}
