package uk.nhs.digital.ps.test.acceptance.steps;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestProperties;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.*;
import uk.nhs.digital.ps.test.acceptance.pages.ConsumableAttachmentElement;
import uk.nhs.digital.ps.test.acceptance.pages.ConsumablePublicationPage;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;
import uk.nhs.digital.ps.test.acceptance.util.FileHelper;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.readFileAsByteArray;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.waitUntilFileAppears;

public class ContentSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(ContentSteps.class);

    @Autowired
    private TestDataRepo testDataRepo;

    @Autowired
    private AcceptanceTestProperties acceptanceTestProperties;

    @Autowired
    private ContentPage contentPage;

    @Autowired
    private LoginSteps loginSteps;

    @Autowired
    private ConsumablePublicationPage consumablePublicationPage;

    // Scenario: New Publication screen ========================================================================
    @Given("^I am on the content page$")
    public void givenIAmOnContentPage() throws Throwable {
        loginSteps.givenIAmLoggedInAsAdmin();
        assertThat("Current page should be content page.", contentPage.openContentTab(), is(true));
    }

    @When("^I create a new publication$")
    public void whenICreateANewPublication() throws Throwable {
        final Publication publication = TestDataFactory.createValidPublication().build();
        testDataRepo.setCurrentPublication(publication);

        assertThat("New publication created.", contentPage.newPublication(publication),
            is(true));
    }

    @Then("^an edit screen is displayed which allows me to populate details of the publication")
    public void thenAnEditScreenIsDisplayed() throws Throwable {
        assertThat("Publication edit screen is displayed",contentPage.isPublicationEditScreenOpen(),
            is(true));
        contentPage.discardUnsavedPublication(testDataRepo.getCurrentPublication().getPublicationName());
    }

    // Scenario: Saving a publication ========================================================================
    @Given("^I am on the edit screen")
    public void givenIamOnTheEditScreen() throws Throwable {
        final Publication publication = TestDataFactory.createValidPublication().build();

        testDataRepo.setCurrentPublication(publication);

        loginSteps.givenIAmLoggedInAsAdmin();
        contentPage.openContentTab();
        contentPage.newPublication(publication);

        // Since previous step created a new document which was not saved, immediately after a login,
        // the edit document screen is displayed (instead of dashboard)
        assertThat("Publication edit screen is displayed",contentPage.isPublicationEditScreenOpen(),
            is(true));
    }

    @When("^I populate and save the publication")
    public void whenIPopulateAndSaveThePublication() throws Throwable {

        contentPage.populatePublication(testDataRepo.getCurrentPublication());
        contentPage.saveAndClosePublication();
    }

    @Then(("^it is saved"))
    public void thenItIsSaved() throws Throwable {
        assertThat("Publication is saved",contentPage.isPublicationSaved(), is(true));
    }


    // Scenario: Publishing a publication ========================================================================
    @Given("^I have saved a publication")
    public void givenIHaveSavedAPublication() throws Throwable {
        loginSteps.givenIAmLoggedInAsAdmin();
        contentPage.openContentTab();
        contentPage.navigateToDocument(testDataRepo.getCurrentPublication().getPublicationName());
    }

    @When("^I publish the publication")
    public void whenIPublishThePublication() throws Throwable {
        contentPage.publish();
        Thread.sleep(1000); // wait for doc to publish

    }

    @Then(("^it is visible to consumers"))
    public void thenItIsVisibleToConsumers() throws Throwable {

        final Publication publication = testDataRepo.getCurrentPublication();

        consumablePublicationPage.open(publication.getPublicationUrlName());

        assertThat("Publication title is as expected",consumablePublicationPage.getTitleText(),
            is(publication.getPublicationTitle()));

        assertThat("Publication summary is as expected",consumablePublicationPage.getSummaryText(),
            is(publication.getPublicationSummary()));

        assertThat("Geographic coverage is as expected",consumablePublicationPage.getGeographicCoverage(),
            is("Geographic coverage:\n" + publication.getGeographicCoverage()));

        assertThat("Publication information type is as expected",consumablePublicationPage.getInformationType(),
            is("Information types:\n" + publication.getInformationType()));

        assertThat("Granularity is as expected",consumablePublicationPage.getGranularity(),
            is("Granularity:\n" + publication.getGranularity()));

        assertThat("Taxonomy is as expected",consumablePublicationPage.getTaxonomy(),
            is("Taxonomy:\n" + publication.getTaxonomy().getTaxonomyContext()));

        assertAttachmentsUpload(publication.getAttachments());
    }

    private void assertAttachmentsUpload(final List<Attachment> attachments) {

        assertThat("Expected number of attachments is displayed.",
            consumablePublicationPage.getAttachments().size(),
            is(attachments.size()));

        attachments.forEach(attachment -> {
            final ConsumableAttachmentElement consumableAttachmentElement =
                consumablePublicationPage.findAttachmentElementByName(attachment.getFullName());

            assertDisplayedAttachmentDetails(attachment, consumableAttachmentElement);

            assertAttachmentDownload(attachment, consumableAttachmentElement);
        });
    }

    private void assertAttachmentDownload(final Attachment attachment, final ConsumableAttachmentElement
        consumableAttachmentElement) {
        if (acceptanceTestProperties.isHeadlessMode()) {
            // At the moment of writing, there doesn't seem to be any easy way available to force Chromedriver
            // to download files when operating in headless mode. It appears that some functionality has been
            // added to DevTools but it's not obvious how to trigger that from Java so, for now at least,
            // we'll only be testing file download when operating in a full, graphical mode.
            //
            // See bug report at https://bugs.chromium.org/p/chromium/issues/detail?id=696481 and other reports
            // available online.
            log.warn("Not testing file download due to running in a headless mode.");
        } else {

            // Trigger file download by click the <a> tag.
            consumableAttachmentElement.clickHyperlink();

            final Path downloadedFilePath = Paths.get(acceptanceTestProperties.getDownloadDir().toString(),
                attachment.getFullName());

            waitUntilFileAppears(downloadedFilePath);

            assertThat("Downloaded file has the same content as the uploaded attachment "
                    + attachment.getFullName(),
                readFileAsByteArray(downloadedFilePath), is(attachment.getContent())
            );
        }
    }

    private void assertDisplayedAttachmentDetails(final Attachment attachment, final ConsumableAttachmentElement
        consumableAttachmentElement) {
        assertThat("Attachment " + attachment.getFullName() +" is displayed",
            consumableAttachmentElement != null, is(true));

        assertThat("Correct size of attachment " + attachment.getFullName() + " is displayed",
            consumableAttachmentElement.getSizeText(),
            is("size: " + FileHelper.toHumanFriendlyFileSize((long) attachment.getContent().length)));
    }

    @When("^I try to upload a file of one of the forbidden types:$")
    public void iTryToUploadAFileOfOneOfTheForbiddenTypes(final DataTable forbiddenExtensions) throws Throwable {

        final List<Attachment> forbiddenAttachments = forbiddenExtensions.asList(String.class).stream()
            .map(extension -> FileType.valueOf(extension.toUpperCase()))
            .map(forbiddenFileType -> TestDataFactory.createAttachmentOfType(forbiddenFileType).build())
            .collect(toList());

        testDataRepo.setCurrentAttachments(forbiddenAttachments);
    }

    @Then("^the upload is rejected with an error message$")
    public void theUploadIsRejectedWithAnErrorMessage() throws Throwable {

        testDataRepo.getCurrentAttachments().forEach(attachment -> {
            contentPage.getAttachmentsSection().uploadAttachment(attachment);

            final String expectedErrorMessage = MessageFormat.format(
                "The uploaded file ''{0}'' has extension {1} which is not allowed.",
                attachment.getFullName(),
                attachment.getFileType().getExtension()
            );

            assertThat("Error message is displayed",
                contentPage.getErrorMessage(), startsWith(expectedErrorMessage));
        });
    }
}
