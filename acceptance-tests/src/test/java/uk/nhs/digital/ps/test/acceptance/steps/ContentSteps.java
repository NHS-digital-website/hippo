package uk.nhs.digital.ps.test.acceptance.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestProperties;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.pages.ConsumablePublicationPage;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.readFileAsByteArray;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.waitUntilFileAppears;

public class ContentSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(ContentSteps.class);

    @Autowired
    private AcceptanceTestProperties acceptanceTestProperties;

    @Autowired
    private ContentPage contentPage;

    @Autowired
    private LoginSteps loginSteps;

    @Autowired
    private ConsumablePublicationPage consumablePublicationPage;

    @Autowired
    private Publication publication;

    // Scenario: New Publication screen ========================================================================
    @Given("^I am on the content page$")
    public void givenIAmOnContentPage() throws Throwable {
        loginSteps.givenIAmLoggedInAsAdmin();
        assertThat("Current page should be content page.", contentPage.openContentTab(), is(true));
    }

    @When("^I create a new publication$")
    public void whenICreateANewPublication() throws Throwable {
        assertThat("New publication created.", contentPage.newPublication(publication.getPublicationName()),
            is(true));
    }

    @Then("^an edit screen is displayed which allows me to populate details of the publication")
    public void thenAnEditScreenIsDisplayed() throws Throwable {
        assertThat("Publication edit screen is displayed",contentPage.isPublicationEditScreenOpen(),
            is(true));
        contentPage.discardUnsavedPublication(publication.getPublicationName());
    }

    // Scenario: Saving a publication ========================================================================
    @Given("^I am on the edit screen")
    public void givenIamOnTheEditScreen() throws Throwable {
        loginSteps.givenIAmLoggedInAsAdmin();
        contentPage.openContentTab();
        contentPage.newPublication(publication.getPublicationName());

        // Since previous step created a new document which was not saved, immediately after a login,
        // the edit document screen is displayed (instead of dashboard)
        assertThat("Publication edit screen is displayed",contentPage.isPublicationEditScreenOpen(),
            is(true));
    }

    @When("^I populate and save the publication")
    public void whenIPopulateAndSaveThePublication() throws Throwable {

        contentPage.populatePublication(publication);
        contentPage.savePublication();
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
        contentPage.navigateToDocument(publication.getPublicationName());
    }

    @When("^I publish the publication")
    public void whenIPublishThePublication() throws Throwable {
        contentPage.publish();
        Thread.sleep(1000); // wait for doc to publish

    }

    @Then(("^it is visible to consumers"))
    public void thenItIsVisibleToConsumers() throws Throwable {

        consumablePublicationPage.open(publication.getPublicationUrl());

        assertThat("Publication title is as expected",consumablePublicationPage.getTitleText(),
            is(publication.getPublicationTitle()));
        assertThat("Publication summary is as expected",consumablePublicationPage.getSummaryText(),
            is(publication.getPublicationSummary()));


        assertThat("Uploaded attachment is available",consumablePublicationPage.getAttachmentName(),
            is(publication.getAttachment().getName()));

        assertThat("Uploaded attachment's size is displayed",consumablePublicationPage.getAttachmentSizeText(),
            is("size: " + publication.getAttachment().getContent().length + " B."));

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

            // Download the file - simulate mouse click on the <a> tag.
            consumablePublicationPage.getAttachmentElement().click();

            final Path downloadedFilePath = Paths.get(acceptanceTestProperties.getDownloadDir().toString(),
                publication.getAttachment().getName());

            waitUntilFileAppears(downloadedFilePath);

            assertThat("Downloaded file has the same content as the uploaded one",
                readFileAsByteArray(downloadedFilePath),
                is(publication.getAttachment().getContent())
            );
        }
    }
}
