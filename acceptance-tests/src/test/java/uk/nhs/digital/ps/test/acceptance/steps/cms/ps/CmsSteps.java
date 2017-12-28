package uk.nhs.digital.ps.test.acceptance.steps.cms.ps;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.data.ExpectedTestDataProvider;
import uk.nhs.digital.ps.test.acceptance.data.TestDataFactory;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.Attachment;
import uk.nhs.digital.ps.test.acceptance.models.FileType;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;
import uk.nhs.digital.ps.test.acceptance.pages.site.SitePage;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPage;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;
import uk.nhs.digital.ps.test.acceptance.steps.cms.LoginSteps;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.test.acceptance.util.AssertionHelper.assertWithinTimeoutThat;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.newRandomString;

public class CmsSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(CmsSteps.class);

    private static final int DAYS_IN_WEEK = 7;

    @Autowired
    private TestDataRepo testDataRepo;

    @Autowired
    private ContentPage contentPage;

    @Autowired
    private LoginSteps loginSteps;

    @Autowired
    private PublicationPage publicationPage;

    @Autowired
    private SitePage sitePage;

    @Given("^I have a publication opened for editing$")
    public void iHaveAPublicationOpenForEditing() throws Throwable {
        final Publication publication = TestDataFactory.createPublicationWithNoAttachments().build();
        testDataRepo.setPublication(publication);

        createPublicationInEditableState(publication);

        // Since previous step created a new document which was not saved, immediately after a login,
        // the edit document screen is displayed (instead of dashboard)
        assertTrue("Publication edit screen is displayed", contentPage.isDocumentEditScreenOpen());
    }

    public void createPublicationInEditableState(final Publication publication) throws Throwable {
        loginSteps.givenIAmLoggedInAsAdmin();
        contentPage.openContentTab();
        contentPage.newPublication(publication);
    }

    public void createPublishedPublication(final Publication publication) throws Throwable {
        createPublicationInEditableState(publication);
        contentPage.populatePublication(publication);
        contentPage.saveAndCloseDocument();
        contentPage.publish();
    }

    // Scenario: New Publication screen ========================================================================
    @Given("^I am on the content page$")
    public void givenIAmOnContentPage() throws Throwable {
        loginSteps.givenIAmLoggedInAsAdmin();
        assertThat("Current page should be content page.", contentPage.openContentTab(), is(true));
    }

    @When("^I create a new publication$")
    public void whenICreateANewPublication() throws Throwable {
        final Publication publication = TestDataFactory.createValidPublication().build();
        testDataRepo.setPublication(publication);

        assertThat("New publication created.", contentPage.newPublication(publication), is(true));
    }

    @Then("^an edit screen is displayed which allows me to populate details of the publication")
    public void thenAnEditScreenIsDisplayed() throws Throwable {
        assertTrue("Publication edit screen is displayed", contentPage.isDocumentEditScreenOpen());
    }

    @When("^I populate and save the publication")
    public void whenIPopulateAndSaveThePublication() throws Throwable {

        contentPage.populatePublication(testDataRepo.getCurrentPublication());
        contentPage.saveAndCloseDocument();
    }

    @Then(("^it is saved"))
    public void thenItIsSaved() throws Throwable {
        assertTrue("Document is saved", contentPage.isDocumentSaved());
    }


    // Scenario: Publishing a publication ========================================================================
    @Given("^I have saved a publication")
    public void givenIHaveSavedAPublication() throws Throwable {
        loginSteps.givenIAmLoggedInAsAdmin();
        contentPage.openContentTab();
        contentPage.navigateToDocument(testDataRepo.getCurrentPublication().getName());
    }

    @When("^I publish the publication")
    public void whenIPublishThePublication() throws Throwable {
        contentPage.publish();
    }

    // Scenario: Forbidden file type upload rejection ==================================================================
    @When("^I try to upload a file of one of the forbidden types:$")
    public void iTryToUploadAFileOfOneOfTheForbiddenTypes(final DataTable forbiddenExtensions) throws Throwable {

        final List<Attachment> forbiddenAttachments = forbiddenExtensions.asList(String.class).stream()
            .map(extension -> FileType.valueOf(extension.toUpperCase()))
            .map(forbiddenFileType -> TestDataFactory.createAttachmentOfType(forbiddenFileType).build())
            .collect(toList());

        testDataRepo.setAttachments(forbiddenAttachments);
    }

    @Then("^the upload is rejected with an error message$")
    public void theUploadIsRejectedWithAnErrorMessage() throws Throwable {

        contentPage.getAttachmentsWidget().addUploadField();

        testDataRepo.getAttachments().forEach(attachment -> {
            contentPage.getAttachmentsWidget().uploadAttachment(attachment);

            final String expectedErrorMessage = MessageFormat.format(
                "The uploaded file ''{0}'' has extension {1} which is not allowed.",
                attachment.getFullName(),
                attachment.getFileType().getExtension()
            );

            assertWithinTimeoutThat("Error message is displayed",
               () -> contentPage.getFirstErrorMessage(), startsWith(expectedErrorMessage));
        });
    }

    // Scenario: Title and Summary validation ========================================================================
    @When("^I populate the title with text longer than the maximum allowed limit of ([0-9]+) characters$")
    public void whenIPopulateTheTitleWithLongText(int lengthLimit) throws Throwable {

        StringBuilder sb = new StringBuilder();
        for (int i=0; i < lengthLimit+1; i++) {
            sb.append(i % 10);
        }
        String longString = sb.toString();

        contentPage.populateDocumentTitle(longString);
    }

    @When("^I save the (?:dataset|publication)$")
    public void iSaveTheDocument() throws Throwable {
        contentPage.saveDocument();
    }

    @Then("^the save is rejected with error message containing \"([^\"]+)\"$")
    public void validationErrorMessageIsShownAndContains(String errorMessageFragment) throws Throwable {

        assertThat("Error message should be shown and contains",
            contentPage.getErrorMessages(),
            hasItem(containsString(errorMessageFragment)));
    }

    // Scenario: Blank attachment field rejection =====================================================================
    @When("^I add an empty upload field$")
    public void iAddAnEmptyUploadField() throws Throwable {
        contentPage.getAttachmentsWidget().addUploadField();
    }

    // Scenario: Blank Granularity field rejection =====================================================================
    @When("^I add an empty Granularity field$")
    public void iAddAnEmptyGranularityField() throws Throwable {
        contentPage.getGranularitySection().addGranularityField();
    }

    // Scenario: Blank related link rejection =========================================================================
    @When("^I add an empty related link field$")
    public void iAddAnEmptyRelatedLinkField() throws Throwable {
        contentPage.getRelatedLinksSection().addRelatedLinkField();
    }

    // Scenario: Blank resource link rejection =========================================================================
    @When("^I add an empty resource link field$")
    public void iAddAnEmptyResourceLinkField() {
        contentPage.getResourceLinksSection().addResourceLinkField();
    }

    // Scenario: Blank Information Type field rejection =====================================================================
    @When("^I add an empty Information Type field$")
    public void iAddAnEmptyInformationTypeField() throws Throwable {
        contentPage.getInformationTypeSection().addInformationTypeField();
    }

    // Scenario: Details are hidden from the end users in a published upcoming publication ==============================
    @Given("^I have a published publication flagged as upcoming$")
    public void iHaveAReleasedPublicationFlaggedAsUpcoming() throws Throwable {
        final Publication publication = ExpectedTestDataProvider.getPublishedUpcomingPublications().build().get(0);
        testDataRepo.setPublication(publication);
    }

    @When("^I view the publication$")
    public void iViewThePublication() throws Throwable {
        publicationPage.open(testDataRepo.getCurrentPublication());
    }

    @Then("^Title is shown$")
    public void titleIsVisible() throws Throwable {
        assertThat("Title is shown.",
            sitePage.getDocumentTitle(), is(testDataRepo.getCurrentPublication().getTitle())
        );
    }

    // Scenarios: Nominal publication date is displayed in full/in part ==============================
    @Then("^Nominal Publication Date field is shown$")
    public void nominalPublicationDateFieldIsVisible() throws Throwable {
        assertThat("Nominal Publication Date field is shown.",
            publicationPage.getNominalPublicationDate(),
            is(testDataRepo.getCurrentPublication().getNominalPublicationDate().formattedInRespectToCutOff())
        );
    }

    @Then("^Disclaimer \"([^\"]*)\" is displayed$")
    public void disclaimerIsDisplayed(final String disclaimer) throws Throwable {
        assertTrue("Disclaimer is displayed: " + disclaimer, publicationPage.hasDisclaimer(disclaimer));
    }

    @Then("^All other publication's details are hidden$")
    public void allOtherDetailsAreHidden() throws Throwable {
        assertThat("Fields that should be hidden for upcoming publication are hidden.",
            publicationPage.getElementsHiddenWhenUpcoming(), is(empty())
        );
    }

    @Given("^I have a published publication with nominal date falling before (\\d+) weeks from now$")
    public void iHaveAPublishedPublicationWithNominalDateFallingBeforeWeeksFromNow(final int weeksFromNow)
        throws Throwable {

        final Publication publicationWithNominalDateBeforeCutOff = TestDataFactory.createPublicationWithNoAttachments()
            .withNominalDate(Instant.now()
                .plus(DAYS_IN_WEEK * weeksFromNow, ChronoUnit.DAYS))
            .build();

        testDataRepo.setPublication(publicationWithNominalDateBeforeCutOff);

        createPublishedPublication(publicationWithNominalDateBeforeCutOff);
    }

    @Given("^I have a published publication with nominal date falling after (\\d+) weeks from now$")
    public void iHaveAPublishedPublicationWithNominalDateFallingAfterWeeksFromNow(final int weeksFromNow)
        throws Throwable {

        final Publication publicationWithNominalDateBeforeCutOff = TestDataFactory.createPublicationWithNoAttachments()
            .withNominalDate(Instant.now()
                .plus(DAYS_IN_WEEK * weeksFromNow, ChronoUnit.DAYS)
                .plus(1, ChronoUnit.DAYS))
            .build();

        testDataRepo.setPublication(publicationWithNominalDateBeforeCutOff);

        createPublishedPublication(publicationWithNominalDateBeforeCutOff);
    }

    @Then("^Nominal Publication Date is displayed using format \"([^\"]*)\"$")
    public void nominalPublicationDateIsDisplayedUsingFormat(final String dateFormat) throws Throwable {

        final Publication.NominalPublicationDate nominalPublicationDate =
            testDataRepo.getCurrentPublication().getNominalPublicationDate();

        final String expectedDate = nominalPublicationDate.inFormat(dateFormat);

        assertThat("Nominal Publication Date is formatted in a way consistent with pattern '" + dateFormat + "'",
            publicationPage.getNominalPublicationDate(), is(expectedDate));
    }

    public void createDatasetInEditableState() throws Throwable {
        loginSteps.givenIAmLoggedInAsAdmin();
        contentPage.openContentTab();
        contentPage.newDataset(testDataRepo.createDatasetName());
    }

    @Given("^I have a dataset opened for editing$")
    public void iHaveADatasetOpenedForEditing() throws Throwable {
        createDatasetInEditableState();
        assertTrue("Dataset edit screen is displayed", contentPage.isDocumentEditScreenOpen());
    }

    @And("^I populate and save the dataset$")
    public void iPopulateAndSaveTheDataset() throws Throwable {
        // these are the only mandatory fields on the dataset.
        contentPage.populateDocumentTitle(newRandomString());
        contentPage.populateDocumentSummary(newRandomString());
        contentPage.saveAndCloseDocument();
    }
}
