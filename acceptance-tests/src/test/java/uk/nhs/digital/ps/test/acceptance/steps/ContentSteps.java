package uk.nhs.digital.ps.test.acceptance.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.pages.ConsumablePublicationPage;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class ContentSteps extends AbstractSpringSteps {

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

    @Then("^an edit screen is displayed which allows me to enter the title and summary for a publication")
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

    @When("^I enter title and summary and save the publication")
    public void whenIEnterTitleAndSummaryAndSaveThePublication() throws Throwable {

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
        assertThat("Publication is displayed on site",consumablePublicationPage.isPageDisplayed(),
            is(true));
        assertThat("Publication title is as expected",consumablePublicationPage.getTitle(),
            is(publication.getPublicationTitle()));
        assertThat("Publication summary is as expected",consumablePublicationPage.getSummary(),
            is(publication.getPublicationSummary()));
    }
}
