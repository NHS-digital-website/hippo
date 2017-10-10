package uk.nhs.digital.ps.test.acceptance.models;

import java.nio.file.Path;

public class Attachment {

    private String name;
    private FileType fileType;
    private byte[] content;
    private Path path;

    Attachment(final AttachmentBuilder builder) {
        name = builder.getName();
        fileType = builder.getFileType();
        content = builder.getContent();
        path = builder.getPath();
    }

    /**
     * @return Full name of the file, including extension if present.
     */
    public String getFullName() {
        return name + (fileType == null ? "" : "." + fileType.getExtension());
    }

    /**
     * @return Content of the file; enriched with type-specific magic numbers where applicable.
     */
    public byte[] getContent() {
        return content;
    }

    public FileType getFileType() {
        return fileType;
    }

    /**
     * @return Path to the actual file backing this attachment: {@code null} if there is no such file.
     */
    public Path getPath() {
        return path;
    }
}
