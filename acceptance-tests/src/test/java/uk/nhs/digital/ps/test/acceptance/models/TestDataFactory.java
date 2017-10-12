package uk.nhs.digital.ps.test.acceptance.models;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.test.acceptance.models.FileType.getAllowedFileTypes;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.getRandomArrayElement;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.newRandomString;

public class TestDataFactory {

    /**
     * @return New instance, fully populated with random or default values.
     */
    public static Publication createValidPublication() {
        return PublicationBuilder.create()
            .withPublicationName(newRandomString())
            .withPublicationTitle(newRandomString())
            .withPublicationSummary(newRandomString())
            .withGeographicCoverage("UK")
            .withInformationType("Experimental statistics")
            .withGranularity("Ambulance Trusts")
            .withAttachments(createValidAttachments())
            .build();
    }

    /**
     * @return New instance of the builder initialised to produce fully populated instance of {@linkplain Attachment}
     *         with default, random, valid values.
     */
    private static AttachmentBuilder createValidAttachment() {

        final FileType fileType = getRandomArrayElement(FileType.getAllowedFileTypes());
        final Path path = getAttachmentFilePathByType(fileType);

        return AttachmentBuilder.create()
            .withName(newRandomString())
            .withFile(path);
    }

    /**
     * @return List of new instances, fully populated with default, random, valid values. The list contains
     *         one attachment per each {@linkplain FileType#getAllowedFileTypes() allowed file type}.
     */
    private static List<Attachment> createValidAttachments() {

        return Arrays.stream(getAllowedFileTypes())
            .map(fileType -> AttachmentBuilder.create()
                .withFile(getAttachmentFilePathByType(fileType))
                .build())
            .collect(toList());
    }

    private static Path getAttachmentFilePathByType(final FileType fileType) {

        final String fullFileName = "attachment." + fileType.getExtension();

        // NOTE: the path is relative to the current module's working directory which is the normal
        // situation when executing tests with Maven. In an unlikely scenario of executing the tests
        // by other means (say, using direct IDE support that bypases Maven), this path may not be valid.
        return Paths.get("src","test","resources","attachments", fullFileName);
    }
}
