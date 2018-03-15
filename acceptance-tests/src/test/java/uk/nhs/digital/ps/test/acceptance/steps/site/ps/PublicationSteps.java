package uk.nhs.digital.ps.test.acceptance.steps.site.ps;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.readFileAsByteArray;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.waitUntilFileAppears;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestProperties;
import uk.nhs.digital.ps.test.acceptance.data.ExpectedTestDataProvider;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.Attachment;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.models.PublicationBuilder;
import uk.nhs.digital.ps.test.acceptance.models.section.BodySection;
import uk.nhs.digital.ps.test.acceptance.pages.site.SitePage;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPage;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.AttachmentWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;
import uk.nhs.digital.ps.test.acceptance.util.FileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

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

        iCanSeeTheSectionedPublicationBody();

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

    @Given("^I have a sectioned publication$")
    public void iHaveASectionedPublication() throws Throwable {
        PublicationBuilder sectionedPublication = ExpectedTestDataProvider.getSectionedPublication();
        testDataRepo.setPublication(sectionedPublication.build());
    }

    @Then("^I can see the sectioned publication body$")
    public void iCanSeeTheSectionedPublicationBody() throws Throwable {
        Publication publication = testDataRepo.getCurrentPublication();

        List<Matcher<? super SectionWidget>> matchers = publication.getBodySections()
            .stream()
            .map(BodySection::getMatcher)
            .collect(Collectors.toList());
        assertThat("Body sections are as expected", publicationPage.getBodySections(), contains(matchers));
    }
}
