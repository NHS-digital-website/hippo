package uk.nhs.digital.ps.test.acceptance.steps.site.ps;

import cucumber.api.java.en.Then;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestProperties;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.*;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.AttachmentElement;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPage;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationSeriesPage;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;
import uk.nhs.digital.ps.test.acceptance.util.FileHelper;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.readFileAsByteArray;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.waitUntilFileAppears;

public class PublicationSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(PublicationSteps.class);

    @Autowired
    private TestDataRepo testDataRepo;

    @Autowired
    private AcceptanceTestProperties acceptanceTestProperties;

    @Autowired
    private ContentPage contentPage;

    @Autowired
    private PublicationPage publicationPage;

    @Autowired
    private PublicationSeriesPage publicationSeriesPage;

    @Then(("^it is visible to consumers"))
    public void thenItIsVisibleToConsumers() throws Throwable {

        final Publication publication = testDataRepo.getCurrentPublication();

        publicationPage.open(publication);

        assertThat("Publication title is as expected", publicationPage.getTitleText(),
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
            is("Taxonomy:\n" + publication.getTaxonomy().getTaxonomyContext()));

        assertAttachmentsUpload(publication.getAttachments());
    }

    private void assertAttachmentsUpload(final List<Attachment> attachments) {

        assertThat("Expected number of attachments is displayed.",
            publicationPage.getAttachments().size(),
            is(attachments.size()));

        attachments.forEach(attachment -> {
            final AttachmentElement attachmentElement =
                publicationPage.findAttachmentElementByName(attachment.getFullName());

            assertDisplayedAttachmentDetails(attachment, attachmentElement);

            assertAttachmentDownload(attachment, attachmentElement);
        });
    }

    private void assertAttachmentDownload(final Attachment attachment,
                                          final AttachmentElement attachmentElement) {

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
        attachmentElement.clickHyperlink();

        final Path downloadedFilePath = Paths.get(acceptanceTestProperties.getDownloadDir().toString(),
            attachment.getFullName());

        waitUntilFileAppears(downloadedFilePath);

        assertThat("Downloaded file has the same content as the uploaded attachment "
                + attachment.getFullName(),
            readFileAsByteArray(downloadedFilePath), is(attachment.getContent())
        );
    }

    private void assertDisplayedAttachmentDetails(final Attachment attachment, final AttachmentElement
        attachmentElement) {
        assertThat("Attachment " + attachment.getFullName() +" is displayed",
            attachmentElement != null, is(true));

        assertThat("Correct size of attachment " + attachment.getFullName() + " is displayed",
            attachmentElement.getSizeText(),
            is("size: " + FileHelper.toHumanFriendlyFileSize((long) attachment.getContent().length)));
    }
}
