package uk.nhs.digital.ps.test.acceptance.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.pages.ConsumableSearchPage;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;

import static org.slf4j.LoggerFactory.getLogger;


public class SearchSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(SearchSteps.class);
    private final static String SEARCH_QUERY = "test";

    @Autowired
    private ConsumableSearchPage consumableSearchPage;

    @Given("^I am on the site homepage$")
    public void givenIamOnTheSiteHomepage() throws Throwable {
        consumableSearchPage.open();
    }

    @When("^I search for a publication$")
    public void whenISearchForAPublication() throws Throwable {
        assertThat("I search for a publication", consumableSearchPage.search(SEARCH_QUERY), is (true));
    }

    @Then("^I want clickable result links which take me to the publication$")
    public void thenIwantClicakableResultLinksWhichTakeMeToThePublications() throws Throwable {
        assertThat("I want clickable result links which take me to the publication", consumableSearchPage.openFirstSearchResult(), is(true));
    }

    @Then("^the results page retains the original search term$")
    public void thenTheResultsPageRetainsTheOriginalSearchTerm() throws Throwable {
        assertThat("The results page retains the original search term", consumableSearchPage.findSearchFieldValue(), is(SEARCH_QUERY));
    }
}

