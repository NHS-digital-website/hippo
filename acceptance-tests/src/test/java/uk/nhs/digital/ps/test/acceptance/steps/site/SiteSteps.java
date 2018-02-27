package uk.nhs.digital.ps.test.acceptance.steps.site;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.test.acceptance.pages.site.AbstractSitePage.URL;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.waitUntilFileAppears;

import cucumber.api.DataTable;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestProperties;
import uk.nhs.digital.ps.test.acceptance.data.TestDataRepo;
import uk.nhs.digital.ps.test.acceptance.models.Taxonomy;
import uk.nhs.digital.ps.test.acceptance.pages.site.SitePage;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;
import uk.nhs.digital.ps.test.acceptance.util.TestContentUrls;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;


public class SiteSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(SiteSteps.class);

    @Autowired
    private SitePage sitePage;

    @Autowired
    private AcceptanceTestProperties acceptanceTestProperties;

    @Autowired
    private TestDataRepo testDataRepo;

    private TestContentUrls urlLookup = new TestContentUrls();

    @Given("^I navigate to (?:the )?\"([^\"]+)\" (?:.* )?page$")
    public void iNavigateToPage(String pageName) throws Throwable {
        sitePage.openByPageName(pageName);
    }

    /**
     *
     * Then I can click on link "foo bar"
     * When I click on "foo bar" button
     */
    @When("^I (?:can )?click on(?: the| link)? \"([^\"]+)\"(?: link| button)?$")
    public void iClickOn(String linkTitle) throws Throwable {
        WebElement element = sitePage.findElementWithTitle(linkTitle);

        assertThat("I can find element with title: " + linkTitle,
            element, is(notNullValue()));

        sitePage.clickOnElement(element);
    }

    @Then("^I (?:can )?see \"([^\"]+)\" (?:link|button|image)$")
    public void iSeeLink(String linkTitle) throws Throwable {
        assertNotNull("Can see element", sitePage.findElementWithTitle(linkTitle));
    }

    @Then("^I should see (?:.* )?page titled \"([^\"]+)\"$")
    public void iShouldSeePageTitled(String pageTitle) throws Throwable {
        assertThat("I should see page titled.", sitePage.getDocumentTitle(), is(pageTitle));
    }

    @And("^I should see the content \"([^\"]*)\"$")
    public void iShouldSeeTheContent(String content) throws Throwable {
        assertThat("Document content is as expected", sitePage.getDocumentContent(), getMatcherForText(content));
    }

    @Then("^I should see the \"page not found\" error page$")
    public void iShouldSeeThePageNotFoundErrorPage() throws Throwable {
        // Ideally we would check the HTTP response code is 404 as well but it's not
        // currently possible to do this with the Selinium Web Driver.
        // See https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/141
        iShouldSeePageTitled("Page not found");
    }

    @Then("^I should (?:also )?see:?$")
    public void iShouldAlsoSee(final DataTable pageSections) throws Throwable {
        String elementName = null;
        for (List<String> elementsContent : pageSections.raw()) {
            elementName = elementsContent.get(0);
            WebElement pageElement = sitePage.findPageElement(elementName);

            assertThat("I should find page element: " + elementName,
                pageElement, is(notNullValue()));

            assertThat("Page element " + elementName + " should contain ...",
                getElementText(pageElement),
                getMatcherForText(elementsContent.get(1)));
        }
    }

    @Then("^I should(?: also)? see \"([^\"]+)\" with:")
    public void iShouldSeeItemsOf(String pageElementName, final DataTable elementItems) throws Throwable  {
        WebElement pageElement = sitePage.findPageElement(pageElementName);

        assertNotNull("I should find page element: " + pageElementName, pageElement);

        for (List<String> elementItem : elementItems.raw()) {
            String expectedItemText = elementItem.get(0);

            assertThat("Page element " + pageElementName + " contain item with text: " + expectedItemText,
                getElementTextList(pageElement),
                hasItem(getMatcherForText(expectedItemText)));
        }
    }

    @Then("^I should not see headers?:$")
    public void iShouldNotSeeHeaders(DataTable headersTable) throws Throwable {
        List<String> headers = headersTable.asList(String.class);
        for (String header : headers) {
            assertNull("Header should not be displayed", sitePage.findElementWithText(header));
        }
    }

    @Then("^I should see headers?:$")
    public void iShouldSeeHeaders(DataTable headersTable) throws Throwable {
        List<String> headers = headersTable.asList(String.class);
        for (String header : headers) {
            assertNotNull("Header should be displayed: " + header, sitePage.findElementWithText(header));
        }
    }

    @Then("^I should(?: also)? see multiple \"([^\"]+)\" with:")
    public void iShouldSeeMultipleItemsOf(String pageElementName, final DataTable elementItems) throws Throwable  {
        int i = 0;

        for (List<String> elementItem : elementItems.raw()) {
            WebElement pageElement = sitePage.findPageElement(pageElementName, i++);
            assertThat("I should find page element: " + pageElementName,
                pageElement, is(notNullValue()));

            String expectedItemText = elementItem.get(0);

            assertThat("Page element " + pageElementName + " contain item with text: " + expectedItemText,
                getElementText(pageElement),
                getMatcherForText(expectedItemText));
        }
    }

    @Then("I can download(?: following files)?:")
    public void iCanDownload(final DataTable downloadTitles) throws Throwable {
        for (List<String> downloadLink : downloadTitles.asLists(String.class)) {
            String linkText = downloadLink.get(0);
            String linkFileName = downloadLink.get(1);

            WebElement downloadElement = sitePage.findElementWithTitle(linkText);

            assertThat("I can find download link with title: " + linkText,
                downloadElement, is(notNullValue()));

            String url = downloadElement.getAttribute("href");
            assertEquals("I can find link with expected URL for file " + linkFileName, URL + urlLookup.lookupUrl(linkFileName), url);

            if (acceptanceTestProperties.isHeadlessMode()) {
                // At the moment of writing, there doesn't seem to be any easy way available to force Chromedriver
                // to download files when operating in headless mode. It appears that some functionality has been
                // added to DevTools but it's not obvious how to trigger that from Java so, for now at least,
                // we'll only be testing file download when operating in a full, graphical mode.
                //
                // See bug report at https://bugs.chromium.org/p/chromium/issues/detail?id=696481 and other reports
                // available online.
                log.warn("Not testing file download due to running in a headless mode, will just check link is present.");
            } else {
                // Trigger file download by click the <a> tag.
                sitePage.clickOnElement(downloadElement);

                final Path downloadedFilePath = Paths.get(acceptanceTestProperties.getDownloadDir().toString(),
                    linkFileName);

                waitUntilFileAppears(downloadedFilePath);
            }
        }
    }

    @Then("^I should see the footer$")
    public void iShouldSeeTheFooter() throws Throwable {
        assertNotNull("Footer is present", sitePage.findFooter());

        assertNotNull("Can find terms and conditions link",
            sitePage.findElementWithTitle("Terms and conditions"));

        assertNotNull("Can find accessibility help link",
            sitePage.findElementWithTitle("Accessibility help"));

        assertNotNull("Can find privacy and cookies link",
            sitePage.findElementWithTitle("Privacy and cookies"));
    }

    /**
     * This is for debug purpose only.
     *
     * Usage:
     *    Given ...
     *    When ...
     *    And I wait 60s
     *    Then step that breaks
     *
     * this gives you 60s to inspect the page
     */
    @Then("^I wait (\\d+)s$")
    public void iWait(int sec) throws InterruptedException {
        Thread.sleep(sec * 1000);
    }

    private static Matcher<String> getMatcherForText(String text) {
        if (text.endsWith(" ...")) {
            return startsWith(text.substring(0, text.length() - 4));
        }

        return is(text);
    }

    private String getElementText(WebElement element) {
        if (element.getTagName().equals("input")) {
            return element.getAttribute("value");
        }

        return element.getText();
    }

    private List<String> getElementTextList(WebElement element) {
        return element.findElements(By.tagName("li")).stream()
            .map(WebElement::getText)
            .collect(toList());
    }

    @Then("^I should see the \"([^\"]*)\" list (containing|(?:not )?including):$")
    public void iShouldSeeTheListWith(String title, String qualifier, DataTable listItems) throws Throwable {
        List<String> items = listItems.asList(String.class);
        listMatchesItems(qualifier, items, sitePage.findPageElement(title));
    }

    @Then("^I should see the list with title \"([^\"]*)\" (containing|(?:not )?including):$")
    public void iShouldSeeTheListWithTitle(String title, String qualifier, DataTable listItems) throws Throwable {
        List<String> items = listItems.asList(String.class);
        listMatchesItems(qualifier, items, sitePage.findElementWithTitle(title));
    }

    private void listMatchesItems(String qualifier, List<String> items, WebElement listElement) {
        assertNotNull("I should find page element", listElement);

        assertThat("List contains items", getElementTextList(listElement), getMatcherForQualifier(qualifier, items));
    }

    private Matcher<Iterable<String>> getMatcherForQualifier(String qualifier, List<String> items) {
        Stream<Matcher<String>> matcherStream = items.stream()
            .map(SiteSteps::getMatcherForText);

        switch (qualifier) {
            case "containing in any order": return (Matcher)containsInAnyOrder(matcherStream.collect(toList()));
            case "containing":    return (Matcher)contains(matcherStream.collect(toList()));
            case "including":     return hasItems(matcherStream.toArray(Matcher[]::new));
            case "not including":
                return not(
                    anyOf(
                        matcherStream
                        .map(Matchers::hasItem)
                        .collect(toList())
                    )
                );

            default: throw new RuntimeException("Unknown qualifier: " + qualifier);
        }
    }

    @Then("^I should not see element with title \"([^\"]*)\"$")
    public void iShouldNotSeeElementTitled(String title) throws Throwable {
        assertNull("Element is not on page", sitePage.findElementWithTitle(title));
    }

    @Then("^I can see the full taxonomy in the faceted navigation$")
    public void iCanSeeTheFullTaxonomyInTheFacetedNavigation() throws Throwable {
        Taxonomy taxonomy = testDataRepo.getCurrentPublication().getTaxonomy();

        List<String> expectedTaxonomy = asList(
            taxonomy.getLevel1() + " (1)",
            taxonomy.getLevel2() + " (1)",
            taxonomy.getLevel3() + " (1)"
        );
        listMatchesItems("containing in any order", expectedTaxonomy, sitePage.findElementWithTitle("CATEGORY"));
    }

    @Then("^I can click on the publication$")
    public void iCanClickOnThePublication() throws Throwable {
        String title = testDataRepo.getCurrentPublication().getTitle();
        iClickOn(title);
    }
}
