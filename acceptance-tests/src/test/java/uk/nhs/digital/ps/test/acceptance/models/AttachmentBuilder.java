package uk.nhs.digital.ps.test.acceptance.models;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.test.acceptance.models.FileType.getAllowedFileTypes;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.getRandomArrayElement;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.newRandomByteArray;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.newRandomString;

// Builder's methods are intentionally not private to promote their use and visibility in IDE outside of this class.
@SuppressWarnings("WeakerAccess")
public class AttachmentBuilder {


    private String name;
    private FileType fileType;
    private byte[] content;

    /** Raw as in 'not enriched with file type specific magic numbers; internal to the builder only. */
    private byte[] rawContent;

    private AttachmentBuilder() {
        // no-op made private to promote the use of factory methods
    }

    String getName() {
        return name;
    }

    FileType getFileType() {
        return fileType;
    }

    byte[] getContent() {
        return content;
    }

    public AttachmentBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public AttachmentBuilder withFileType(final FileType fileType) {
        this.fileType = fileType;
        // ensure that the content is enriched with type-specific magic numbers
        if (rawContent != null) {
            content = fileType.enrichWithMagicNumbers(rawContent);
        }
        return this;
    }

    public AttachmentBuilder withContent(final byte[] rawContent) {
        this.rawContent = rawContent;
        // ensure that the content is enriched with type-specific magic numbers
        this.content = fileType == null ? this.rawContent : fileType.enrichWithMagicNumbers(this.rawContent);
        return this;
    }

    public Attachment build() {
        return new Attachment(this);
    }

    /**
     * @return New instance of the builder initialised to produce fully populated instance of {@linkplain Attachment}
     *         with default, random, valid values.
     */
    public static AttachmentBuilder createValidAttachment() {

        final FileType fileType = getRandomArrayElement(FileType.values());

        return new AttachmentBuilder()
            .withName(newRandomString())
            .withContent(newRandomByteArray())
            .withFileType(fileType);
    }

    /**
     * @return List of new instances, fully populated with default, random, valid values. The list contains
     *         one attachment per each {@linkplain FileType#getAllowedFileTypes() allowed file type}.
     */
    public static List<Attachment> createValidAttachments() {

        return Arrays.stream(getAllowedFileTypes())
            .map(extension -> AttachmentBuilder.createValidAttachment().withFileType(extension).build())
            .collect(toList());
    }
}
