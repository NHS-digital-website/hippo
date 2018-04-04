package uk.nhs.digital.ps.test.acceptance.data;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.test.acceptance.models.FileType.getAllowedFileTypes;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.getRandomEnumConstant;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.newRandomString;

import uk.nhs.digital.ps.test.acceptance.models.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * Provides methods creating commonly used test data objects on demand used to initialise the tests and serving as
 * 'expected' data, reference in assertions made in the tests.
 * </p><p>
 * Preferably, the instances of the test data objects should be populated with unique or randomised values.
 * </p>
 */
public class TestDataFactory {

    /**
     * @return New instance, fully populated with random or default values.
     */
    public static PublicationBuilder createFullyPopulatedPublication() {
        return createBareMinimumPublication()
            .withGeographicCoverage(getRandomEnumConstant(GeographicCoverage.class))
            .withInformationType(getRandomEnumConstant(InformationType.class))
            .withGranularity(getRandomEnumConstant(Granularity.class))
            .withTaxonomy(Taxonomy.createNew("Conditions", "Accidents and injuries", "Falls"))
            .withAttachments(createValidAttachments());
    }

    /**
     * Create a publication with only the required fields populated.
     * This saves quite a lot of time when running the tests as we don't have to
     * use the taxonomy/granularity picker etc.
     */
    public static PublicationBuilder createBareMinimumPublication() {
        return PublicationBuilder.newPublication()
            .withName(newRandomString())
            .withTitle(newRandomString())
            .withSummary(newRandomString())
            .withNominalDate(Instant.now())
            .withPubliclyAccessible(true);
    }

    public static PublicationSeriesBuilder createSeries() {
        return PublicationSeriesBuilder.newPublicationSeries()
            .withName(newRandomString() + " Series")
            .withTitle(newRandomString() + " Series Title")
            .withSummary(newRandomString() + " Series Summary");
    }

    public static PublicationArchiveBuilder createArchive() {
        return PublicationArchiveBuilder.newPublicationArchive()
            .withName(newRandomString() + " Archive")
            .withTitle(newRandomString() + " Archive Title")
            .withSummary(newRandomString() + " Archive Summary");
    }

    public static DatasetBuilder createDataset() {
        return DatasetBuilder.newDataset()
            .withName(newRandomString() + " Dataset")
            .withTitle(newRandomString() + " Dataset Title")
            .withSummary(newRandomString() + " Dataset Summary")
            .withNominalDate(Instant.now());
    }

    public static Folder createFolder() {
        return new Folder("Corporate Website/Publication System/Acceptance Tests", newRandomString());
    }

    public static AttachmentBuilder createAttachmentOfType(final FileType fileType) {
        final Path path = getAttachmentFilePathByType(fileType);

        return AttachmentBuilder.newAttachment()
            .withName(newRandomString())
            .withFile(path);
    }

    /**
     * @return List of new instances, fully populated with default, random, valid values. The list contains
     * one attachment per each {@linkplain FileType#getAllowedFileTypes() allowed file type}.
     */
    private static List<AttachmentBuilder> createValidAttachments() {

        return Arrays.stream(getAllowedFileTypes())
            .map(TestDataFactory::createAttachmentOfType)
            .collect(toList());
    }

    static Path getAttachmentFilePathByType(final FileType fileType) {

        final String fullFileName = "attachment." + fileType.getExtension();

        // NOTE: the path is relative to the current module's working directory which is the normal
        // situation when executing tests with Maven. In an unlikely scenario of executing the tests
        // by other means (say, using direct IDE support that bypases Maven), this path may not be valid.
        return Paths.get("src", "test", "resources", "attachments", fullFileName);
    }
}
