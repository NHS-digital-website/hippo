package uk.nhs.digital.ps.test.acceptance.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.data.ExpectedTestDataProvider;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.Publication;
import uk.nhs.digital.ps.test.acceptance.models.PublicationSeries;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPage;
import uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationSeriesPage;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class PublicationSeriesSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(PublicationSeriesSteps.class);

    @Autowired
    private TestDataRepo testDataRepo;

    @Autowired
    private PublicationPage publicationPage;

    @Autowired
    private PublicationSeriesPage consumablePublicationSeriesPage;

    // Scenario: List publications from the same series ===========================================================
    @Given("^I have a number of publications belonging to the same series$")
    public void iHaveANumberOfPublicationsBelongingToTheSameSeries() throws Throwable {

        final PublicationSeries publicationSeries = ExpectedTestDataProvider.getValidPublicationSeries().build();
        testDataRepo.setPublicationSeries(publicationSeries);
    }

    @When("^I navigate to the publication series$")
    public void iNavigateToThePublicationSeries() throws Throwable {
        consumablePublicationSeriesPage.open(testDataRepo.getPublicationSeries());
    }

    @Then("^I can see a list of published publications from the series$")
    public void iCanSeeAListOfPublicationsFromTheSeries() throws Throwable {

        final PublicationSeries expectedPublicationSeries = testDataRepo.getPublicationSeries();

        assertThat("Correct publication series title is displayed.",
            consumablePublicationSeriesPage.getSeriesTitle(), is(expectedPublicationSeries.getTitle()));

        final List<String> expectedPublicationTitles = expectedPublicationSeries.getReleasedPublicationsLatestFirst().stream()
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

    @Then("^I can see link back to the series")
    public void iCanSeeLinkBack() throws Throwable {
        assertThat("Link to series document .",
            publicationPage.getSeriesLinkTitle(), is(testDataRepo.getPublicationSeries().getTitle()));
    }

}
