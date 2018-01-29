package uk.nhs.digital.ps.test.acceptance.steps.site;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.pages.site.common.SearchPage;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SearchResultWidget;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;

import java.util.List;


public class SearchSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(SearchSteps.class);

    @Autowired
    private SearchPage searchPage;

    @Autowired
    private TestDataRepo testDataRepo;

    @When("^I search for \"(.*)\"$")
    public void iSearchFor(String searchTerm) throws Throwable {
        searchPage.searchForTerm(searchTerm);
    }

    @When("^I search for the publication$")
    public void iSearchForThePublication() throws Throwable {
        String title = testDataRepo.getCurrentPublication().getTitle();
        iSearchFor(title);
    }

    @Then("^I should see publication in search results$")
    public void iShouldSeePublicationInSearchResults() throws Throwable {
        String expectedTitle = testDataRepo.getCurrentPublication().getTitle();

        List<String> actualResults = searchPage.getSearchResultWidgets()
            .stream()
            .map(SearchResultWidget::getTitle)
            .collect(toList());

        assertThat("Publication is in the results", actualResults, hasItem(expectedTitle));
    }

    @Then("^I should not see publication in search results$")
    public void iShouldNotSeePublicationInSearchResults() throws Throwable {
        String expectedTitle = testDataRepo.getCurrentPublication().getTitle();

        List<String> actualResults = searchPage.getSearchResultWidgets()
            .stream()
            .map(SearchResultWidget::getTitle)
            .collect(toList());

        assertThat("Publication is not in the results", actualResults, not(hasItem(expectedTitle)));
    }

    @Then("^I should see (\\d+) search results?$")
    public void iShouldSeeSearchResults(String count) throws Throwable {
        if (count.equals("0")) {
            count = "No results";
        }
        assertThat("Correct result count found", searchPage.getResultCount(), startsWith(count));
    }

    @Then("^I should see search results starting with:$")
    public void iShouldSeeSearchResultsStartingWith(DataTable searchResults) throws Throwable {

        List<String> actualResults = searchPage.getSearchResultWidgets()
            .stream()
            .map(SearchResultWidget::getTitle)
            .collect(toList());

        List<Matcher<? super String>> expectedResults = searchResults.asList(String.class)
            .stream()
            .map(Matchers::startsWith)
            .collect(toList());

        assertThat("Search results match", actualResults, contains(expectedResults));
    }

    @Then("^I should see search results which also include:$")
    public void iShouldSeeSearchResultsWhichAlsoInclude(DataTable expectedResults) throws Throwable {

        List<SearchResultWidget> actualResults = searchPage.getSearchResultWidgets();

        for (List<String> elementItem : expectedResults.raw()) {
            assertTrue("Search result includes item specified",
                actualResults.stream()
                    .anyMatch(result->result.getType().equals(elementItem.get(0))
                    && result.getTitle().equals(elementItem.get(1))));
        }
    }

    @Then("^I should see the search term \"(.+)\" on the results page$")
    public void iShouldSeeTheSearchTermOnTheResultsPage(String term) throws Throwable {
        assertThat("Result count ends with search term", searchPage.getResultCount(), endsWith(term));
        assertEquals("Search query is maintained in search box", term, searchPage.getSearchFieldValue());
    }

    @Then("^I should see the full search results page$")
    public void iShouldSeeTheFullSearchResultsPage() throws Throwable {
        assertThat("Full search results are shown", searchPage.getResultCount(), endsWith("results found"));
        assertEquals("Search query is blank", "", searchPage.getSearchFieldValue());
    }

}
