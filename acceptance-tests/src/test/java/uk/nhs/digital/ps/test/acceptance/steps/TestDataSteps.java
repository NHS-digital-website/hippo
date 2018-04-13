package uk.nhs.digital.ps.test.acceptance.steps;

import static org.slf4j.LoggerFactory.getLogger;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestProperties;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.Dataset;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.models.PublicationArchive;
import uk.nhs.digital.ps.test.acceptance.models.PublicationSeries;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;

import java.io.IOException;
import java.io.UncheckedIOException;

public class TestDataSteps extends AbstractSpringSteps {

    private static final Logger log = getLogger(TestDataSteps.class);

    @Autowired
    private ContentPage contentPage;

    @Autowired
    private TestDataRepo testDataRepo;

    @Autowired
    private AcceptanceTestProperties acceptanceTestProperties;

    /**
     * Resets the test data repository before every scenario to prevent data leaking between scenarios, unless given
     * scenario is tagged with {@code @NeedsExistingTestData}.
     */
    @Before(value = "~@NeedsExistingTestData")
    public void clearTestData() {
        log.debug("Disposing of test data.");
        testDataRepo.clear();

        try {
            FileUtils.deleteDirectory(acceptanceTestProperties.getDownloadDir().toFile());
        } catch (IOException ioe) {
            throw new UncheckedIOException(ioe);
        }
    }

    /**
     * <p>
     * Closes the current publication discarding current changes.
     * </p><p>
     * Only applicable to scenarios that leave the document in editable state; implemented as an {@linkplain After}
     * hook rather than a step because this is really a test cleanup/tear-down activity that doesn't warrant expressing
     * explicitly as a scenario step.
     * </p><p>
     * To ensure that this method gets called at the end of your scenario, tag the scenario with
     * {@code @DiscardAfter}.
     * </p>
     */
    @After(value = "@DiscardAfter", order = 500)
    public void discardEditedDocument() throws Throwable {
        Publication currentPublication = testDataRepo.getCurrentPublication();
        if (currentPublication != null) {
            final String currentPublicationName = currentPublication.getName();
            log.debug("Discarding and closing current publication: {}.", currentPublicationName);
            contentPage.discardUnsavedChanges(currentPublicationName);
        }

        Dataset dataset = testDataRepo.getDataset();
        if (dataset != null) {
            String datasetName = dataset.getName();
            log.debug("Discarding and closing current dataset: {}.", datasetName);
            contentPage.discardUnsavedChanges(datasetName);
        }

        PublicationSeries series = testDataRepo.getPublicationSeries();
        if (series != null) {
            final String currentSeriesName = series.getName();
            log.debug("Discarding and closing current series: {}.", currentSeriesName);
            contentPage.discardUnsavedChanges(currentSeriesName);
        }

        PublicationArchive archive = testDataRepo.getPublicationArchive();
        if (archive != null) {
            final String currentArchiveName = archive.getName();
            log.debug("Discarding and closing current archive: {}.", currentArchiveName);
            contentPage.discardUnsavedChanges(currentArchiveName);
        }
    }

    /**
     * <p>
     * Takes current publication offline (un-publishes it) and deletes it.
     * </p><p>
     * Only applicable to scenarios leaving a published document behind.
     * </p><p>
     * To ensure that this method gets called at the end of your scenario, tag the scenario with
     * {@code @DeleteAfter}.
     * </p>
     */
    @After(value = "@DeleteAfter", order = 500)
    public void deleteDocument() throws Throwable {
        // Don't want this to fail the test if it fails, it's just clean up
        try {
            String currentDocumentName = "";
            if (testDataRepo.getCurrentPublication() != null) {
                currentDocumentName = testDataRepo.getCurrentPublication().getName();
            } else if (testDataRepo.getDataset() != null) {
                currentDocumentName = testDataRepo.getDataset().getName();
            } else if (testDataRepo.getPublicationSeries() != null) {
                currentDocumentName = testDataRepo.getPublicationSeries().getName();
            } else if (testDataRepo.getPublicationArchive() != null) {
                currentDocumentName = testDataRepo.getPublicationArchive().getName();
            }

            log.debug("Taking current document offline: {}.", currentDocumentName);

            contentPage.openCms();
            contentPage.openContentTab();
            contentPage.navigateToDocument(currentDocumentName);
            contentPage.unpublishDocument();
            contentPage.deleteDocument();

        } catch (Exception exception) {
            log.error("Failed to take publication offline.", exception);
        }
    }
}
