package uk.nhs.digital.ps.test.acceptance.models;

public class Attachment {

    private String name;
    private FileType fileType;
    private byte[] content;

    Attachment(final AttachmentBuilder builder) {
        name = builder.getName();
        fileType = builder.getFileType();
        content = builder.getContent();
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
}
