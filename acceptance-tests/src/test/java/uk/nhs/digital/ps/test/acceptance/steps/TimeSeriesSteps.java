package uk.nhs.digital.ps.test.acceptance.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestProperties;
import uk.nhs.digital.ps.test.acceptance.data.TestDataLoader;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.*;
import uk.nhs.digital.ps.test.acceptance.pages.ConsumablePublicationPage;
import uk.nhs.digital.ps.test.acceptance.pages.ConsumablePublicationSeriesPage;
import uk.nhs.digital.ps.test.acceptance.pages.ContentPage;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class TimeSeriesSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(TimeSeriesSteps.class);

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

    @Autowired
    private ConsumablePublicationSeriesPage consumablePublicationSeriesPage;

    // Scenario: List publications from the same time series ===========================================================
    @Given("^I have a number of publications belonging to the same time series$")
    public void iHaveANumberOfPublicationsBelongingToTheSameTimeSeries() throws Throwable {

        final TimeSeries timeSeries = TestDataLoader.loadValidTimeSeries().build();
        testDataRepo.setCurrentTimeSeries(timeSeries);
    }

    @When("^I navigate to the time series$")
    public void iNavigateToTheTimeSeries() throws Throwable {
        consumablePublicationSeriesPage.open(testDataRepo.getCurrentTimeSeries());
    }

    @Then("^I can see a list of released publications from the time series$")
    public void iCanSeeAListOfPublicationsFromTheTimeSeries() throws Throwable {

        final TimeSeries expectedTimeSeries = testDataRepo.getCurrentTimeSeries();

        assertThat("Correct publication time series title is displayed.",
            consumablePublicationSeriesPage.getSeriesTitle(), is(expectedTimeSeries.getTitle()));

        final List<String> expectedPublicationTitles = expectedTimeSeries.getReleasedPublicationsLatestFirst().stream()
            .map(Publication::getTitle)
            .collect(toList());

        assertThat("Correct publication titles are displayed in order.",
            consumablePublicationSeriesPage.getPublicationTitles(),
            is(expectedPublicationTitles));
    }

    @When("^I navigate to the first publication")
    public void iNavigateToTheFirstPublication() throws Throwable {
        consumablePublicationSeriesPage.openFirstPublication();
    }

    @Then("^I can see link back to the time series")
    public void iCanSeeLinkBack() throws Throwable {
        assertThat("Link to series document .",
            consumablePublicationPage.getSeriesLinkTitle(), is(testDataRepo.getCurrentTimeSeries().getTitle()));
    }

}
