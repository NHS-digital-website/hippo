package uk.nhs.digital.ps.test.acceptance.steps.site.ps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestProperties;
import uk.nhs.digital.ps.test.acceptance.data.ExpectedTestDataProvider;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.Attachment;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.pages.site.SitePage;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPage;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationsOverviewPage;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.AttachmentWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.LivePublicationOverviewWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.UpcomingPublicationOverivewWidget;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;
import uk.nhs.digital.ps.test.acceptance.util.FileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.test.acceptance.data.TestDataRepo.PublicationClassifier.LIVE;
import static uk.nhs.digital.ps.test.acceptance.data.TestDataRepo.PublicationClassifier.UPCOMING;
import static uk.nhs.digital.ps.test.acceptance.pages.widgets.LivePublicationOverviewWidget.matchesLivePublication;
import static uk.nhs.digital.ps.test.acceptance.pages.widgets.UpcomingPublicationOverivewWidget.matchesUpcomingPublication;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.readFileAsByteArray;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.waitUntilFileAppears;

public class PublicationSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(PublicationSteps.class);

    @Autowired
    private TestDataRepo testDataRepo;

    @Autowired
    private AcceptanceTestProperties acceptanceTestProperties;

    @Autowired
    private PublicationPage publicationPage;

    @Autowired
    private SitePage sitePage;

    @Autowired
    private PublicationsOverviewPage publicationsOverviewPage;

    @Given("^Published and upcoming publications are available in the system$")
    public void publishedAndUpcomingPublicationsAreAvailableInTheSystem() throws Throwable {
        testDataRepo.addPublications(UPCOMING, ExpectedTestDataProvider.getPublishedUpcomingPublications().build());
        testDataRepo.addPublications(LIVE, ExpectedTestDataProvider.getRecentPublishedLivePublications().build());
    }

    @Then(("^it is visible to consumers"))
    public void thenItIsVisibleToConsumers() throws Throwable {

        final Publication publication = testDataRepo.getCurrentPublication();

        publicationPage.open(publication);

        assertThat("Publication title is as expected", sitePage.getDocumentTitle(),
            is(publication.getTitle()));

        assertThat("Publication summary is as expected", publicationPage.getSummaryText(),
            is(publication.getSummary()));

        assertThat("Geographic coverage is as expected", publicationPage.getGeographicCoverage(),
            is(publication.getGeographicCoverage().getDisplayValue()));

        assertThat("Publication information type is as expected", publicationPage.getInformationType(),
            is(publication.getInformationType().getDisplayName()));

        assertThat("Granularity is as expected", publicationPage.getGranularity(),
            is(publication.getGranularity().getDisplayValue()));

        assertThat("Taxonomy is as expected", publicationPage.getTaxonomy(),
            is(publication.getTaxonomy().getTaxonomyContext()));

        assertAttachmentsUpload(publication.getAttachments());
    }

    private void assertAttachmentsUpload(final List<Attachment> attachments) {

        assertThat("Expected number of attachments is displayed.",
            publicationPage.getAttachments().size(),
            is(attachments.size()));

        attachments.forEach(attachment -> {
            final AttachmentWidget attachmentWidget =
                publicationPage.findAttachmentElementByName(attachment.getFullName());

            assertDisplayedAttachmentDetails(attachment, attachmentWidget);

            assertAttachmentDownload(attachment, attachmentWidget);
        });
    }

    private void assertAttachmentDownload(final Attachment attachment,
                                          final AttachmentWidget attachmentWidget) {

        if (acceptanceTestProperties.isHeadlessMode()) {
            // At the moment of writing, there doesn't seem to be any easy way available to force Chromedriver
            // to download files when operating in headless mode. It appears that some functionality has been
            // added to DevTools but it's not obvious how to trigger that from Java so, for now at least,
            // we'll only be testing file download when operating in a full, graphical mode.
            //
            // See bug report at https://bugs.chromium.org/p/chromium/issues/detail?id=696481 and other reports
            // available online.

            log.warn("Not testing file download due to running in a headless mode.");
            return;
        }

        // Trigger file download by click the <a> tag.
        attachmentWidget.clickHyperlink();

        final Path downloadedFilePath = Paths.get(acceptanceTestProperties.getDownloadDir().toString(),
            attachment.getFullName());

        waitUntilFileAppears(downloadedFilePath);

        assertThat("Downloaded file has the same content as the uploaded attachment "
                + attachment.getFullName(),
            readFileAsByteArray(downloadedFilePath), is(attachment.getContent())
        );
    }

    private void assertDisplayedAttachmentDetails(final Attachment attachment, final AttachmentWidget
        attachmentWidget) {
        assertThat("Attachment " + attachment.getFullName() +" is displayed",
            attachmentWidget != null, is(true));

        assertThat("Correct size of attachment " + attachment.getFullName() + " is displayed",
            attachmentWidget.getSizeText(),
            is("size: " + FileHelper.toHumanFriendlyFileSize((long) attachment.getContent().length)));
    }

    @Then("^I can see upcoming publications$")
    public void iCanSeeUpcomingPublications() throws Throwable {

        final List<Publication> expectedPublications = testDataRepo.getPublications(UPCOMING);

        final List<UpcomingPublicationOverivewWidget> actualPublicationEntries =
            publicationsOverviewPage.getUpcomingPublicationsWidgets();

        assertThat("Should display correct quantity of upcoming publications.",
            actualPublicationEntries,
            hasSize(expectedPublications.size())
        );

        for (int i = 0; i < expectedPublications.size(); i++) {
            assertThat("Should display upcoming publication.",
                actualPublicationEntries.get(i),
                matchesUpcomingPublication(expectedPublications.get(i))
            );
        }
    }

    @Then("^I can see recent publications$")
    public void iCanSeeRecentPublications() throws Throwable {

        final List<Publication> expectedPublications = testDataRepo.getPublications(LIVE);

        final List<LivePublicationOverviewWidget> actualPublicationEntries =
            publicationsOverviewPage.getLatestPublicationsWidgets();

        // Making this test more relaxed, just check that 5 recent publications are shown in the list of 10.
        // This means when we publish new publications this test should still pass.
        for (Publication expectedPublication : expectedPublications) {
            assertThat("Recent publication should be shown.",
                actualPublicationEntries,
                hasItem(matchesLivePublication(expectedPublication))
            );
        }
    }
}
