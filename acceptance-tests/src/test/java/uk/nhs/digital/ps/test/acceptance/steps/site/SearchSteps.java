package uk.nhs.digital.ps.test.acceptance.steps.site;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.pages.site.common.SearchPage;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SearchResultWidget;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;

import java.util.List;


public class SearchSteps extends AbstractSpringSteps {

    @Autowired
    private SearchPage searchPage;

    @Autowired
    private TestDataRepo testDataRepo;

    @When("^I search for \"(.*)\"$")
    public void whenISearchFor(String searchTerm) throws Throwable {
        searchPage.searchForTerm(searchTerm);
    }

    @When("^I search for the publication$")
    public void whenISearchForThePublication() throws Throwable {
        String title = testDataRepo.getCurrentPublication().getTitle();
        whenISearchFor(title);
    }

    @When("^I search for a publication taxonomy term$")
    public void whenISearchForAPublicationTaxonomyTerm() throws Throwable {
        String taxonomy = testDataRepo.getCurrentPublication().getTaxonomy().getLevel3();
        whenISearchFor(taxonomy);
    }

    @Then("^I should see publication in search results$")
    public void thenIShouldSeePublicationInSearchResults() throws Throwable {
        String expectedTitle = testDataRepo.getCurrentPublication().getTitle();

        List<String> actualResults = searchPage.getSearchResultWidgets()
            .stream()
            .map(SearchResultWidget::getTitle)
            .collect(toList());

        assertThat("Publication is in the results", actualResults, hasItem(expectedTitle));
    }

    @Then("^I should not see publication in search results$")
    public void thenIShouldNotSeePublicationInSearchResults() throws Throwable {
        String expectedTitle = testDataRepo.getCurrentPublication().getTitle();

        List<String> actualResults = searchPage.getSearchResultWidgets()
            .stream()
            .map(SearchResultWidget::getTitle)
            .collect(toList());

        assertThat("Publication is not in the results", actualResults, not(hasItem(expectedTitle)));
    }

    @Then("^I should see (\\d+) search results?$")
    public void thenIShouldSeeSearchResults(String count) throws Throwable {
        assertThat("Correct result count found", searchPage.getResultCount(), startsWith(count));
    }

    @Then("^I should see search results starting with:$")
    public void thenIShouldSeeSearchResultsStartingWith(DataTable searchResults) throws Throwable {
        assertSearchResultsStartWith(searchResults.asList(String.class));
    }

    @Then("^I should see search results which also include:$")
    public void thenIShouldSeeSearchResultsWhichAlsoInclude(DataTable expectedResults) throws Throwable {

        List<SearchResultWidget> actualResults = searchPage.getSearchResultWidgets();

        for (List<String> elementItem : expectedResults.raw()) {
            assertTrue("Search result includes item specified",
                actualResults.stream()
                    .anyMatch(result -> result.getType().equals(elementItem.get(0))
                        && result.getTitle().equals(elementItem.get(1))));
        }
    }

    @Then("^I should see search results which (have|doesnt have) the national statistics logo")
    public void thenIShouldSeeSearchResultsWithTheNationalStatisticsLogo(String hasLogo) throws Throwable {
        List<SearchResultWidget> actualResults = searchPage.getSearchResultWidgets();

        actualResults.stream()
            .forEach(result -> {
                assertTrue("Search result with national statistic logo",
                    result.isNationalStatistic() == hasLogo.equals("have"));
            });
    }

    @Then("^I should see the search term \"(.+)\" on the results page$")
    public void thenIShouldSeeTheSearchTermOnTheResultsPage(String term) throws Throwable {
        assertThat("Result description ends with search term", searchPage.getResultDescription(), endsWith(term));
        assertEquals("Search query is maintained in search box", term, searchPage.getSearchFieldValue());
    }

    @Then("^I can see the search description matching \"([^\"]*)\"$")
    public void thenICanSeeTheSearchDescriptionMatching(String regex) throws Throwable {
        String resultDescription = searchPage.getResultDescription();
        assertTrue("Result description [" + resultDescription + "] matches", resultDescription.matches(regex));
    }

    @Then("^I should see the weight search test results ordered by (relevance|date)$")
    public void thenIShouldSeeTheWeightSearchTestResultsOrderedBy(String sort) throws Throwable {
        // check the results are actually sorted correctly
        assertSearchResultsStartWith(getSortedWeightSearchResults(sort));

        assertEquals("Sort mode is displayed in selector", capitalize(sort), searchPage.getSortSelection());

        assertThat("Sort mode is displayed in result description", searchPage.getResultDescription(), endsWith("sorted by " + sort + "."));
    }

    private static List<String> getSortedWeightSearchResults(String sort) {
        switch (sort) {
            case "relevance":
                return getWeightSearchResultsSortedByRelevance();
            case "date":
                return getWeightSearchResultsOrderedByDate();
            default:
                throw new RuntimeException("Unknown sort mode: " + sort);
        }
    }

    private void assertSearchResultsStartWith(List<String> searchResults) {
        List<String> actualResults = searchPage.getSearchResultWidgets()
            .stream()
            .map(SearchResultWidget::getTitle)
            .collect(toList());

        List<Matcher<? super String>> expectedResults = searchResults
            .stream()
            .map(Matchers::startsWith)
            .collect(toList());

        assertThat("Search results match", actualResults, contains(expectedResults));
    }

    private static List<String> getWeightSearchResultsSortedByRelevance() {
        return asList(
            // Search results should go Series -> Publications -> Datasets -> Archives internally ordered by date
            "Search Test Series Summary",
            "Search Test Series Title WeightSearchTerm",
            "Search Test Publication Summary Apr 18",
            "Search Test Publication Title Dec 17",
            "Search Test Publication Key Facts Feb 18",
            "Search Test Dataset Summary Mar 18",
            "Search Test Dataset Title Jan 18",
            "Search Test Archive Summary",
            "Search Test Archive Title",
            "Search Test Indicator Title "
        );
    }

    private static List<String> getWeightSearchResultsOrderedByDate() {
        return asList(
            // These are in date order
            "Search Test Publication Summary Apr 18",
            "Search Test Dataset Summary Mar 18",
            "Search Test Publication Key Facts Feb 18",
            "Search Test Dataset Title Jan 18",
            "Search Test Publication Title Dec 17",
            "Search Test Indicator Definition",
            "Search Test Indicator Title",
            "Search Test Series Summary",
            "Search Test Series Title",
            "Search Test Archive Summary"
        );
    }
}
