package uk.nhs.digital.ps.test.acceptance.steps.cms.ps;

import static java.time.temporal.ChronoField.*;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.test.acceptance.util.AssertionHelper.assertWithinTimeoutThat;
import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.getRandomInt;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.data.ExpectedTestDataProvider;
import uk.nhs.digital.ps.test.acceptance.data.TestDataFactory;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.*;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;
import uk.nhs.digital.ps.test.acceptance.pages.site.SitePage;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPage;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;
import uk.nhs.digital.ps.test.acceptance.steps.cms.LoginSteps;

import java.text.MessageFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.List;

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
        final Publication publication = TestDataFactory.createBareMinimumPublication().build();
        testDataRepo.setPublication(publication);

        createPublicationInEditableState(publication);

        // Since previous step created a new document which was not saved, immediately after a login,
        // the edit document screen is displayed (instead of dashboard)
        assertTrue("Publication edit screen is displayed", contentPage.isDocumentEditScreenOpen());
    }

    public void createPublicationInEditableState(final Publication publication) throws Throwable {
        loginSteps.loginAsCiEditor();
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
        loginSteps.loginAsCiEditor();
        assertThat("Current page should be content page.", contentPage.openContentTab(), is(true));
    }

    @When("^I create a fully populated publication$")
    public void whenICreateANewPublication() throws Throwable {
        final Publication publication = TestDataFactory.createFullyPopulatedPublication().build();
        testDataRepo.setPublication(publication);

        assertThat("New publication created.", contentPage.newPublication(publication), is(true));
    }

    @When("^I create a publication with taxonomy$")
    public void whenICreateANewPublicationWithTaxonomy() throws Throwable {
        final Publication publication = TestDataFactory.createBareMinimumPublication()
            .withTaxonomy(Taxonomy.createNew("Conditions", "Accidents and injuries", "Falls"))
            .build();
        testDataRepo.setPublication(publication);

        assertThat("New publication created.", contentPage.newPublication(publication), is(true));
    }

    @Then("^an edit screen is displayed which allows me to populate details of the publication")
    public void thenAnEditScreenIsDisplayed() throws Throwable {
        assertTrue("Publication edit screen is displayed", contentPage.isDocumentEditScreenOpen());
    }

    @When("^I populate and save the publication")
    public void whenIPopulateAndSaveThePublication() throws Throwable {
        iPopulateThePublication();
        iSaveTheDocument();
    }

    @Then(("^it is saved"))
    public void thenItIsSaved() throws Throwable {
        assertTrue("Document is saved", contentPage.isDocumentSaved());
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

    @When("^I navigate to CMS$")
    public void navigateToCms() {
        contentPage.openCms();
    }

    @When("^I open publication for editing$")
    public void openPublicationForEditing() {
        contentPage.openDocumentForEdit(testDataRepo.getCurrentPublication().getName());
    }

    @When("^I set publication as upcoming$")
    public void setPublicationAsUpcoming() {
        contentPage.getPubliclyAccessibleWidget().select(false);
    }

    @When("^I save the (?:archive|dataset|publication|series)$")
    public void iSaveTheDocument() throws Throwable {
        // Always save and close as all the steps either want to test the save is
        // blocked with validation, or close the saved document and do something else
        contentPage.saveAndCloseDocument();
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

        createPublicationInEditableState(publication);
        whenIPopulateAndSaveThePublication();
        whenIPublishThePublication();
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

    // Scenarios: Publication Date is displayed in full/in part ==============================
    @Then("^Publication Date field is shown$")
    public void nominalPublicationDateFieldIsVisible() throws Throwable {
        assertThat("Publication Date field is shown.",
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

    @Given("^I have a published publication with nominal date falling before (-?\\d+) (days|weeks|years) from now$")
    public void iHaveAPublishedPublicationWithNominalDateFallingBeforeWeeksFromNow(final int valueFromNow, final String unit)
        throws Throwable {

        ChronoUnit chronoUnit = ChronoUnit.valueOf(unit.toUpperCase());
        final Publication publicationWithNominalDateBeforeCutOff = TestDataFactory.createBareMinimumPublication()
            .withNominalDate(LocalDateTime.now()
                .plus(valueFromNow, chronoUnit)
                .toInstant(ZoneOffset.UTC))
            .build();

        testDataRepo.setPublication(publicationWithNominalDateBeforeCutOff);

        createPublishedPublication(publicationWithNominalDateBeforeCutOff);
    }

    @Given("^I have a published publication with nominal date falling this (week|month|year)$")
    public void iHaveAPublishedPublicationWithNominalDateFallingThis(String unit) throws Throwable {
        LocalDateTime date;
        if (unit.equals("week")) {
            // we want a day that is this week but not today or yesterday
            date = getDateRelativeToToday(DAY_OF_WEEK, 1);
        } else if (unit.equals("month")) {
            // we want a date that is this month but not this week, so don't pick a date 7 days either side or today
            date = getDateRelativeToToday(DAY_OF_MONTH, DAYS_IN_WEEK);
        } else {
            // we want a date that is this year but not this week or month, so don't pick a date one month either side
            date = getDateRelativeToToday(MONTH_OF_YEAR, 1);
        }

        final Publication publication = TestDataFactory.createBareMinimumPublication()
            .withNominalDate(date.toInstant(ZoneOffset.UTC))
            .build();

        testDataRepo.setPublication(publication);

        createPublishedPublication(publication);
    }

    private LocalDateTime getDateRelativeToToday(ChronoField field, int buffer) {
        LocalDateTime date = LocalDateTime.now();
        // want a date with a buffer either side of it and also not including the date itself
        int gap = 2 * buffer + 1;
        int max = (int)field.range().getSmallestMaximum();
        int rand = getRandomInt(1, max - gap);
        int value = rand >= date.get(field) - buffer ? rand + gap : rand;
        LocalDateTime relativeDate = date.with(field, value);

        // Adding logging to help with some spurious errors...
        log.info("Date: " + date + " rand: " + rand + " value: " + value + " Picked date: " + relativeDate);

        return relativeDate;
    }

    @Given("^I have a published publication with nominal date falling after (\\d+) weeks from now$")
    public void iHaveAPublishedPublicationWithNominalDateFallingAfterWeeksFromNow(final int weeksFromNow)
        throws Throwable {

        final Publication publicationWithNominalDateBeforeCutOff = TestDataFactory.createBareMinimumPublication()
            .withNominalDate(Instant.now()
                .plus(DAYS_IN_WEEK * weeksFromNow, ChronoUnit.DAYS)
                .plus(1, ChronoUnit.DAYS))
            .build();

        testDataRepo.setPublication(publicationWithNominalDateBeforeCutOff);

        createPublishedPublication(publicationWithNominalDateBeforeCutOff);
    }

    @Then("^Publication Date is displayed using format \"([^\"]*)\"$")
    public void nominalPublicationDateIsDisplayedUsingFormat(final String dateFormat) throws Throwable {

        final Publication.NominalPublicationDate nominalPublicationDate =
            testDataRepo.getCurrentPublication().getNominalPublicationDate();

        final String expectedDate = nominalPublicationDate.inFormat(dateFormat);

        assertThat("Publication Date is formatted in a way consistent with pattern '" + dateFormat + "'",
            publicationPage.getNominalPublicationDate(), is(expectedDate));
    }

    public void createSeriesInEditableState() throws Throwable {
        loginSteps.loginAsCiEditor();
        contentPage.openContentTab();

        final PublicationSeries publicationSeries = TestDataFactory.createSeries().build();
        testDataRepo.setPublicationSeries(publicationSeries);
        contentPage.newSeries(publicationSeries);
    }

    @Given("^I have a series opened for editing$")
    public void iHaveASeriesOpenedForEditing() throws Throwable {
        createSeriesInEditableState();
        assertTrue("Series edit screen is displayed", contentPage.isDocumentEditScreenOpen());
    }

    public void createArchiveInEditableState() throws Throwable {
        loginSteps.loginAsCiEditor();
        contentPage.openContentTab();

        final PublicationArchive publicationArchive = TestDataFactory.createArchive().build();
        testDataRepo.setPublicationArchive(publicationArchive);
        contentPage.newArchive(publicationArchive);
    }

    @Given("^I have an archive opened for editing$")
    public void iHaveAnArchiveOpenedForEditing() throws Throwable {
        createArchiveInEditableState();
        assertTrue("Archive edit screen is displayed", contentPage.isDocumentEditScreenOpen());
    }

    private void createDatasetInEditableState() throws Throwable {
        loginSteps.loginAsCiEditor();
        contentPage.openContentTab();

        Dataset dataset = TestDataFactory.createDataset().build();
        testDataRepo.setDataset(dataset);
        contentPage.newDataset(dataset);
    }

    @When("^I create a new folder$")
    public void iCreateANewFolder() throws Throwable {
        Folder folder = TestDataFactory.createFolder();
        testDataRepo.setFolder(folder);
        contentPage.createFolder(folder);
    }

    @Given("^I have a dataset opened for editing$")
    public void iHaveADatasetOpenedForEditing() throws Throwable {
        createDatasetInEditableState();
        assertTrue("Dataset edit screen is displayed", contentPage.isDocumentEditScreenOpen());
    }

    @And("^I populate and save the dataset$")
    public void iPopulateAndSaveTheDataset() throws Throwable {
        iPopulateTheDataset();
        iSaveTheDocument();
    }

    @And("^I populate the dataset$")
    public void iPopulateTheDataset() throws Throwable {
        contentPage.populateDataset(testDataRepo.getDataset());
    }

    @When("^I populate the publication$")
    public void iPopulateThePublication() throws Throwable {
        contentPage.populatePublication(testDataRepo.getCurrentPublication());
    }

    @And("^I populate the series$")
    public void iPopulateTheSeries() throws Throwable {
        contentPage.populateSeries(testDataRepo.getPublicationSeries());
    }

    @And("^I populate the archive$")
    public void iPopulateTheArchive() throws Throwable {
        contentPage.populateArchive(testDataRepo.getPublicationArchive());
    }

    /**
     * So we can populate all the fields in a document and then clear a specific one.
     * Need this to test the validation for mandatory fields in isolation as the error
     * message is the same for different fields.
     */
    @When("^I clear the title$")
    public void iClearTheTitle() throws Throwable {
        contentPage.findTitleElement().clear();
    }

    @When("^I clear the summary$")
    public void iClearTheSummary() throws Throwable {
        contentPage.findSummaryElement().clear();
    }

    @When("^I clear the nominal date")
    public void iClearTheNominalDate() throws Throwable {
        contentPage.findNominalDateField().clear();
    }

    @When("^I click the \"([^\"]*)\" menu option on the \"([^\"]*)\" folder$")
    public void iOpenTheMenuOnTheFolder(String menuOption, String folder) throws Throwable {
        String[] folders = folder.split("/");
        contentPage.clickFolderMenuOption(menuOption, folders);
    }

    @And("^I shouldn't have a \"([^\"]*)\" menu option on the \"([^\"]*)\" folder$")
    public void iShouldntHaveAMenuOption(String menuOption, String folder) throws Throwable {
        String[] folders = folder.split("/");
        assertNull("No document type picker", contentPage.getFolderMenuItem(menuOption, folders));
    }

    @When("^I click the \"([^\"]*)\" menu option on the folder$")
    public void iOpenTheMenuOnTheFolder(String menuOption) throws Throwable {
        iOpenTheMenuOnTheFolder(menuOption, testDataRepo.getFolder().getPath());
    }

    @Then("^The \"([^\"]*)\" folder should have the menu options( not)? including:$")
    public void theFolderShouldHaveTheMenuOptionsIncluding(String folder, String not, DataTable optionsData) throws Throwable {
        String[] folders = folder.split("/");
        List<String> actualOptions = contentPage.getFolderMenuOptions(folders);

        List<Matcher> matchers = optionsData.asList(String.class).stream()
            .map(Matchers::hasItem)
            .collect(toList());

        Matcher<List> matcher;
        if (isEmpty(not)) {
            matcher = allOf(matchers.toArray(new Matcher[0]));
        } else {
            matcher = not(anyOf(matchers.toArray(new Matcher[0])));
        }

        assertThat("Menu options on the folder are as expected", actualOptions, matcher);
    }

    @Then("^I should see the document options:$")
    public void iShouldSeeTheDocumentOptions(DataTable options) throws Throwable {
        List<String> expectedDocumentTypes = options.asList(String.class);
        List<String> actualOptions = contentPage.getDocumentTypeOptions();

        assertThat("Document option are as expected", actualOptions, containsInAnyOrder(expectedDocumentTypes.toArray()));
    }

    @Then("^I should see no document options$")
    public void iShouldSeeNoDocumentOptions() throws Throwable {
        assertNull("No document picker is shown", contentPage.getDocumentTypeOptions());
    }

    @Given("^I am logged in as ([^\"]*) on the content page$")
    public void givenIAmLoggedInAsOnTheContentPage(String user) throws Throwable {
        loginSteps.loginAs(user);
        contentPage.openContentTab();
    }

    @And("^I should( not)? see the \"([^\"]*)\" folder$")
    public void iShouldSeeTheFolder(String not, String folder) throws Throwable {
        String[] folders = folder.split("/");
        boolean expectedPresent = isEmpty(not);
        assertThat("Folder presence is as expected", contentPage.navigateToFolder(folders) != null, is(expectedPresent));
    }

    @Then("^I can copy the publication$")
    public void iCanCopyThePublication() throws Throwable {
        String docName = testDataRepo.getCurrentPublication().getName();
        contentPage.navigateToDocument(docName);
        contentPage.copyDocument();
    }
}
