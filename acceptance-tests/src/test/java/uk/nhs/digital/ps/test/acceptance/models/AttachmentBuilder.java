package uk.nhs.digital.ps.test.acceptance.models;

import org.apache.commons.io.FilenameUtils;
import uk.nhs.digital.ps.test.acceptance.util.FileHelper;

import java.nio.file.Path;
import java.text.MessageFormat;

@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class AttachmentBuilder {

    private String name;
    private FileType fileType;
    private byte[] content;
    private Path path;

    public static AttachmentBuilder newAttachment() {
        return new AttachmentBuilder();
    }

    //<editor-fold desc="BUILDER METHODS">
    public AttachmentBuilder withName(final String name) {
        vetSettingProperty("name");

        return cloneAndAmend(builder -> builder.name = name);
    }

    public AttachmentBuilder withFileType(final FileType fileType) {
        vetSettingProperty("fileType");

        return cloneAndAmend(builder -> builder.fileType = fileType);
    }

    public AttachmentBuilder withContent(final byte[] content) {
        vetSettingProperty("content");

        return cloneAndAmend(builder -> builder.content = content);
    }

    public AttachmentBuilder withFile(final Path path) {

        final byte[] content = FileHelper.readFileAsByteArray(path);
        final FileType fileType = FileHelper.getFileType(path);
        final String fileName = FilenameUtils.getBaseName(path.getFileName().toString());

        return cloneAndAmend(builder -> {
            builder.path = path;
            builder.content = content;
            builder.fileType = fileType;
            builder.name = fileName;
        });
    }

    public Path getPath() {
        return path;
    }
    //</editor-fold>

    public Attachment build() {
        return new Attachment(this);
    }

    //<editor-fold desc="GETTERS" defaultstate="collapsed">
    String getName() {
        return name;
    }

    FileType getFileType() {
        return fileType;
    }

    byte[] getContent() {
        return content;
    }
    //</editor-fold>

    private AttachmentBuilder() {
        // no-op; made private to promote the use of factory methods
    }

    private AttachmentBuilder(final AttachmentBuilder original) {
        name = original.getName();
        fileType = original.getFileType();
        content = original.getContent();
        path = original.getPath();
    }

    private AttachmentBuilder cloneAndAmend(final PropertySetter propertySetter) {
        final AttachmentBuilder clone = new AttachmentBuilder(this);
        propertySetter.setProperties(clone);
        return clone;
    }

    private void vetSettingProperty(final String propertyName) {
        if (path != null) {
            throw new IllegalStateException(MessageFormat.format(
                "Setting of {0} is not permitted as this attachment is already backed by an actual file and " +
                    "changing its {0} would leave it in inconsistent state. To set arbitrary {0} use use a builder " +
                    "without the path set.", propertyName)
            );
        }
    }

    @FunctionalInterface
    interface PropertySetter {

        void setProperties(AttachmentBuilder builder);
    }
}
