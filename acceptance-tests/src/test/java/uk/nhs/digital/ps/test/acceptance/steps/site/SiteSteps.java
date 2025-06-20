package uk.nhs.digital.ps.test.acceptance.steps.site;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.*;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.waitUntilFileAppears;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;


public class SiteSteps extends AbstractSpringSteps {

    private static final Logger log = getLogger(SiteSteps.class);

    @Autowired
    private SitePage sitePage;

    @Autowired
    private AcceptanceTestProperties acceptanceTestProperties;

    @Autowired
    private TestDataRepo testDataRepo;

    @Autowired
    private TestContentUrls urlLookup;

    @Given("^I navigate to (?:the )?\"([^\"]+)\" (?:.* )?page$")
    public void givenINavigateToPage(String pageName) throws Throwable {
        sitePage.openByPageName(pageName);

        // Clear the cookie banner if it's up
        sitePage.clickCookieAcceptButton();
    }

    /**
     * Then I can click on link "foo bar"
     * When I click on "foo bar" button
     */
    @When("^I (?:can )?click on(?: the| link)? \"([^\"]+)\"(?: link| button)?$")
    public void whenIClickOn(String linkTitle) throws Throwable {
        WebElement element = sitePage.findElementWithTitle(linkTitle);

        assertThat("I can find element with title: " + linkTitle,
            element, is(notNullValue()));

        sitePage.clickOnElement(element);

        // Note: this is temporary while we have some pages that don't have the new cookie banner (old RPS style)
        sitePage.clickCookieAcceptButton();
    }

    @When("^I click on the \"([^\"]*)\" labelled button$")
    public void whenIClickOnLinkLabelled(String linkLabel) throws Throwable {
        WebElement element = sitePage.findElementWithLabel(linkLabel);

        assertThat("I can find element with label: " + linkLabel,
            element, is(notNullValue()));

        sitePage.clickOnElement(element);

        // Note: this is temporary while we have some pages that don't have the new cookie banner (old RPS style)
        sitePage.clickCookieAcceptButton();
    }

    @When("^I click on the \"([^\"]*)\" labelled tag$")
    public void whenIClickOnTagLabelled(String title) throws Throwable {
        WebElement element = sitePage.findElementWithTitleAndClass(title, "nhsd-a-tag");

        assertThat("I can find element with title: " + title,
            element, is(notNullValue()));

        sitePage.clickOnElement(element);

        // Note: this is temporary while we have some pages that don't have the new cookie banner (old RPS style)
        sitePage.clickCookieAcceptButton();
    }

    @When("^I click on the (?:link|button) named \"([^\"]+)\"$")
    public void whenIClickOnTheLinkNamed(String linkName) throws Throwable {
        WebElement element = sitePage.findLinkWithText(linkName);

        assertThat("I can find link with name: " + linkName,
            element, is(notNullValue()));

        sitePage.clickOnElement(element);

        // Note: this is temporary while we have some pages that don't have the new cookie banner (old RPS style)
        sitePage.clickCookieAcceptButton();
    }

    @When("^I (?:can )?click on the label for \"([^\"]+)\"$")
    public void whenIClickOnLabel(String labelledElement) throws Throwable {
        String xPathExpression = buildXPathExpressionFromElementAttributes(
            Collections.singletonList("for"),
            Collections.singletonList(labelledElement)
        );
        WebElement element = sitePage.findElementWithXPath(xPathExpression);

        assertThat("I can find element with title: " + labelledElement,
            element, is(notNullValue()));

        sitePage.clickOnElement(element);

        // Note: this is temporary while we have some pages that don't have the new cookie banner (old RPS style)
        sitePage.clickCookieAcceptButton();
    }

    @When("^I enter search term \"([^\"]+)\"$")
    public void whenIEnterSearchTerm(String searchTerm) throws Throwable {
        WebElement element = sitePage.findElementWithID("catalogue-search-bar-input");
        sitePage.clickOnElement(element);
        element.clear();
        element.sendKeys(searchTerm);
    }

    @Then("^I should see the search result with the id \"([^\"]+)\"$")
    public void thenIShouldSeeSearchResult(String id) throws Throwable {
        WebElement element = sitePage.findElementWithID(id);
        WebElement elementParent = element.findElement(By.xpath("./.."));
        boolean isHidden = elementParent.getAttribute("class").equals("nhsd-!t-display-hide");
        assertThat(isHidden, is(false));
    }

    @Then("^I should not see the search result \"([^\"]+)\"$")
    public void thenIShouldNotSeeSearchResult(String id) throws Throwable {
        WebElement element = sitePage.findElementWithID(id);
        WebElement elementParent = element.findElement(By.xpath("./.."));
        boolean isHidden = elementParent.getAttribute("class").equals("nhsd-!t-display-hide");
        assertThat(isHidden, is(true));
    }

    @Then("^I should see the search result with the id \"([^\"]*)\" highlighted in yellow colour$")
    public void thenIShouldSearchResultHighlightedInYellow(String id) throws Throwable {
        WebElement element = sitePage.findElementWithID(id);
        WebElement childElement = element.findElement(By.xpath("./child::*"));
        WebElement grandChildElement = childElement.findElement(By.xpath("./child::*"));
        boolean isPresent = grandChildElement.getAttribute("class").equals("filter-tag-yellow-highlight");
        assertThat(isPresent, is(true));
    }

    @Then("^I (?:can )?see \"([^\"]+)\" (?:link|button|image)$")
    public void thenISeeLink(String linkTitle) throws Throwable {
        assertNotNull("Can see element", sitePage.findElementWithTitle(linkTitle));
    }

    @Then("^I (?:can )?see labelled element \"([^\"]+)\" with content \"([^\"]+)\"$")
    public void thenISeeElementWithUiPathAndContent(String uiPath, String content) throws Throwable {
        assertNotNull("Can see element with data-uipath: " + uiPath, sitePage.findElementWithUiPath(uiPath));
        assertThat(sitePage.findElementWithUiPath(uiPath).getText(), containsString(content));
    }

    @Then("^I (?:can )?see (?:.* )?with data-uipath \"([^\"]+)\"$")
    public void thenISeeElementWithUiPath(String uiPath) throws Throwable {
        assertNotNull("Can see element with data-uipath: " + uiPath, sitePage.findElementWithUiPath(uiPath));
    }

    @Then("^I can not see (?:.* )?with data-uipath \"([^\"]+)\"$")
    public void thenICanNotSeeElementWithUiPath(String uiPath) throws Throwable {
        assertNull("Can not see element with data-uipath: " + uiPath, sitePage.findOptionalElementWithUiPath(uiPath));
    }

    @Then("^I should see (?:.* )?page titled \"([^\"]+)\"$")
    public void thenIShouldSeePageTitled(String pageTitle) throws Throwable {
        assertThat("I should see page titled.", sitePage.getDocumentTitle(), is(pageTitle));
    }

    @Then("^I should see (?:.* )?page summary \"([^\"]+)\"$")
    public void thenIShouldSeePageSummary(String pageSummary) throws Throwable {
        assertThat("I should see page summary.", sitePage.getDocumentSummary(), getMatcherForText(pageSummary));
    }

    @Then("^I should see the content \"([^\"]*)\"$")
    public void thenIShouldSeeTheContent(String content) throws Throwable {
        assertThat("Document content is as expected", sitePage.getDocumentContent(), getMatcherForText(content));
    }

    @Then("^I should see page with text  \"([^\"]*)\"$")
    public void thenIShouldSeeArticleContent(String content) throws Throwable {
        assertThat("Page contains text '" + content + "'", sitePage.findElementWithText(content), is(notNullValue()));
    }

    @Then("^I should see the page not found error page$")
    public void thenIShouldSeeThePageNotFoundErrorPage() throws Throwable {
        // Ideally we would check the HTTP response code is 404 as well but it's not
        // currently possible to do this with the Selinium Web Driver.
        // See https://github.com/seleniumhq/selenium-google-code-issue-archive/issues/141
        thenIShouldSeePageTitled("We can't seem to find the page you're looking for");
    }

    @Then("^I should (?:also )?see elements with ui path:?$")
    public void thenIShouldAlsoSeeWithUiPath(final DataTable pageSections) throws Throwable {
        String uiPath = null;
        for (List<String> contentData : pageSections.cells()) {
            uiPath = contentData.get(0);
            WebElement pageElement = sitePage.findElementWithUiPath(uiPath);

            assertThat("I should find page element with UI path: " + uiPath,
                pageElement, is(notNullValue()));

            assertThat("Page element " + uiPath + " should contain ...",
                getElementText(pageElement),
                getMatcherForText(contentData.get(1)));
        }
    }

    @Then("^I should (?:also )?see:?$")
    public void thenIShouldAlsoSee(final DataTable pageSections) throws Throwable {
        String elementName = null;
        for (List<String> elementsContent : pageSections.cells()) {
            elementName = elementsContent.get(0);
            WebElement pageElement = sitePage.findPageElement(elementName);

            assertThat("I should find page element: " + elementName,
                pageElement, is(notNullValue()));

            assertThat("Page element " + elementName + " should contain ...",
                getElementText(pageElement),
                getMatcherForText(elementsContent.get(1)));
        }
    }

    @Then("^I should not see:?$")
    public void thenIShouldNotSee(final DataTable pageSections) throws Throwable {
        String elementName = null;
        for (List<String> elementsContent : pageSections.cells()) {
            elementName = elementsContent.get(0);
            WebElement pageElement = sitePage.findPageElement(elementName);

            assertThat("I should not have found the page element: " + elementName,
                pageElement, is(nullValue()));
        }
    }

    @Then("^I should(?: also)? see \"([^\"]+)\" items with:")
    public void thenIShouldSeeItemsWith(String itemName, final DataTable elementItems) throws Throwable  {
        List<WebElement> pageElements = sitePage.findPageElements(itemName);

        List<String> elementsText = pageElements.stream()
            .map(WebElement::getText)
            .filter(StringUtils::isNotBlank)
            .collect(toList());

        for (List<String> elementItem : elementItems.cells()) {
            String expectedItemText = elementItem.get(0);

            assertThat("Page contains " + itemName + " item with text: " + expectedItemText,
                elementsText,
                hasItem(getMatcherForText(expectedItemText)));
        }
    }

    @Then("^I should(?: also)? see \"([^\"]+)\" with:")
    public void thenIShouldSeeItemsOf(String pageElementName, final DataTable elementItems) throws Throwable  {
        WebElement pageElement = sitePage.findPageElement(pageElementName);

        assertNotNull("I should find page element: " + pageElementName, pageElement);

        for (List<String> elementItem : elementItems.cells()) {
            String expectedItemText = elementItem.get(0);

            assertThat("Page element " + pageElementName + " contain item with text: " + expectedItemText,
                getElementTextList(pageElement),
                hasItem(getMatcherForText(expectedItemText)));
        }
    }

    @Then("^I should see section \"([^\"]+)\" with hyperlink \"([^\"]+)\"")
    public void thenIShouldSeeSectionWithLink(String sectionUiPathSuffix, String linkName) throws Throwable  {
        final String nilLandingSectionUiDatapathPrefix = "nil.landing.section.";

        WebElement pageElement = sitePage.findLinkWithinUiPath(
            nilLandingSectionUiDatapathPrefix + sectionUiPathSuffix, linkName
        );

        assertNotNull("I should find page link: " + linkName + " within: " + sectionUiPathSuffix, pageElement);
    }

    @Then("^I should not see headers?:$")
    public void thenIShouldNotSeeHeaders(DataTable headersTable) throws Throwable {
        List<String> headers = headersTable.asList(String.class);
        for (String header : headers) {
            header = header.replaceAll("^\"|\"$", "");
            assertNull("Header should not be displayed", sitePage.findOptionalElementWithText(header));
        }
    }

    @Then("^I should see headers?:$")
    public void thenIShouldSeeHeaders(DataTable headersTable) throws Throwable {
        List<String> headers = headersTable.asList(String.class);
        for (String header : headers) {
            header = header.replaceAll("^\"|\"$", "");
            assertNotNull("Header should be displayed: " + header, sitePage.findElementWithText(header));
        }
    }

    @Then("^I should see the following:$")
    public void thenIShouldSeeTheFollowing(DataTable contentsTable) throws Throwable {
        List<String> headers = contentsTable.asList(String.class);
        for (String header : headers) {
            assertNotNull("The Text should be displayed: " + header, sitePage.findElementWithText(header));
        }
    }

    @Then("^I should see headers that starts with the following:$")
    public void thenIShouldAHeaderThatStartsWithTheFollowing(DataTable contentsTable) throws Throwable {
        List<String> headers = contentsTable.asList(String.class);
        for (String header : headers) {
            header = header.replaceAll("^\"|\"$", "");
            assertNotNull("The text should start with the following: " + header, sitePage.findElementThatContainsText(header));
        }
    }

    @Then("^the index is rendered with entries:")
    public void thenIndexIsRendered(final DataTable elementAttributes) throws Throwable {
        thenIShouldSeeMultipleElementsWithAttributes(elementAttributes);
    }

    @Then("^I can see the following links:")
    public void thenICanSeeLinks(final DataTable elementAttributes) throws Throwable {
        thenIShouldSeeMultipleElementsWithAttributes(elementAttributes);
    }

    @Then("^I (?:can|should) see elements with attributes:")
    public void thenIShouldSeeMultipleElementsWithAttributes(final DataTable elementAttributes) throws Throwable {
        final List<List<String>> rawElementItems = elementAttributes.cells();
        List<String> keys = rawElementItems.get(0);
        for (int i = 1; i < rawElementItems.size(); i++) {
            List<String> elementItem = rawElementItems.get(i);

            String xPathExpression = buildXPathExpressionFromElementAttributes(keys, elementItem);

            assertNotNull(
                "I should find element with xpath expression " + xPathExpression,
                sitePage.findElementWithXPath(xPathExpression)
            );
        }
    }

    @Then("^I can see an update alert")
    public void thenICanSeeUpdate() throws Throwable {
        assertNotNull("I can see an update alert", sitePage.findCssClass("nhsd-m-emphasis-box"));
    }

    private String buildXPathExpressionFromElementAttributes(List<String> keys, List<String> elementItem) {
        List<String> clauses = new ArrayList<>();
        for (String key: keys) {
            String value = elementItem.get(keys.indexOf(key));
            if (key.equals("text")) {
                String clause = String.format( "text()=\"%s\"", value);
                clauses.add(clause);
                continue;
            }
            String clause = String.format("@%s=\"%s\"", key, value);
            clauses.add(clause);
        }
        return String.format("//*[%s]", String.join(" and ", clauses));
    }

    @Then("^I should(?: also)? see multiple \"([^\"]+)\" with:")
    public void thenIShouldSeeMultipleItemsOf(String pageElementName, final DataTable elementItems) throws Throwable  {

        final List<List<String>> rawElementItems = elementItems.cells();
        for (int i = 0; i < rawElementItems.size(); i++) {
            List<String> elementItem = rawElementItems.get(i);
            WebElement pageElement = sitePage.findPageElement(pageElementName, i);
            assertThat("I should find page element: " + pageElementName,
                pageElement, is(notNullValue()));

            String expectedItemText = elementItem.get(0);

            assertThat(
                "Page element " + pageElementName + " contain item with text: " + expectedItemText,
                getElementText(pageElement),
                getMatcherForText(expectedItemText));
        }
    }

    @Then("I can download following files:")
    public void thenICanDownload(final DataTable downloadTitles) throws Throwable {
        for (List<String> downloadLink : downloadTitles.asLists(String.class)) {
            String linkText = downloadLink.get(0);
            String linkFileName = downloadLink.get(1);

            WebElement downloadElement = sitePage.findLinkWithText(linkText);

            assertThat("I can find download link with text: " + linkText,
                downloadElement, is(notNullValue()));

            String url = downloadElement.getAttribute("href");
            assertThat("I can find link with expected URL for file " + linkFileName,
                url, is(urlLookup.lookupSiteUrl(linkFileName))
            );

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
    public void thenIShouldSeeTheFooter() throws Throwable {
        assertNotNull("Footer is present", sitePage.findFooter());

        assertNotNull("Can find terms and conditions link",
            sitePage.findElementWithText("Terms and conditions"));

        assertNotNull("Can find accessibility link",
            sitePage.findElementWithText("Accessibility"));

        assertNotNull("Can find privacy and cookies link",
            sitePage.findElementWithText("Privacy and cookies"));
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
    public void thenIWait(int sec) throws InterruptedException {
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
            .filter(StringUtils::isNotBlank)
            .collect(toList());
    }

    @Then("^I should see the \"([^\"]*)\" list (containing|(?:not )?including):$")
    public void thenIShouldSeeTheListWith(String title, String qualifier, DataTable listItems) throws Throwable {
        List<String> items = listItems.asList(String.class);
        listMatchesItems(qualifier, items, sitePage.findPageElement(title));
    }

    @Then("^I should see the \"([^\"]*)\" list (containing|(?:not )?including) \"([A-Z,]*)\"$")
    public void thenIShouldSeeTheListWithAlphabet(String title, String qualifier, String alphabet) throws Throwable {
        List<String> items = Arrays.asList(alphabet.split(","));
        listMatchesItems(qualifier, items, sitePage.findPageElement(title));
    }

    @Then("^I should see the list with title \"([^\"]*)\" (containing|(?:not )?including):$")
    public void thenIShouldSeeTheListWithTitle(String title, String qualifier, DataTable listItems) throws Throwable {
        List<String> items = listItems.asList(String.class);
        listMatchesItems(qualifier, items, sitePage.findElementWithTitle(title));
    }

    @Then("^I should see the list with UI path \"([^\"]*)\" (containing|(?:not )?including):$")
    public void thenIShouldSeeTheListWithUiPath(String path, String qualifier, DataTable listItems) throws Throwable {
        List<String> items = listItems.asList(String.class);
        listMatchesItems(qualifier, items, sitePage.findElementWithUiPath(path));
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
    public void thenIShouldNotSeeElementTitled(String title) throws Throwable {
        assertNull("Element is not on page", sitePage.findOptionalElementWithTitle(title));
    }

    @Then("^I can see the full taxonomy in the faceted navigation$")
    public void thenICanSeeTheFullTaxonomyInTheFacetedNavigation() throws Throwable {
        Taxonomy taxonomy = testDataRepo.getCurrentPublication().getTaxonomy();

        List<String> expectedTaxonomy = asList(
            taxonomy.getLevel1() + " ...",
            taxonomy.getLevel2() + " ...",
            taxonomy.getLevel3() + " (1)"
        );

        // Navigate through the tree
        whenIClickOn(taxonomy.getLevel1());
        whenIClickOn(taxonomy.getLevel2());

        listMatchesItems("containing in any order", expectedTaxonomy, sitePage.findElementWithTitle("CATEGORY/TOPIC"));

        // Reset back to having none selected in the tree
        whenIClickOn(taxonomy.getLevel1());
    }

    @Then("^I can click on the publication$")
    public void thenICanClickOnThePublication() throws Throwable {
        String title = testDataRepo.getCurrentPublication().getTitle();
        whenIClickOn(title);
    }

    @Then("^I should see side navigation$")
    public void thenIShouldSeeSideNavigation() throws Throwable {
        assertNotNull("Can find side navigation",
            sitePage.findElementWithText("Page contents"));
    }

    @Then("^I should not see side navigation$")
    public void thenIShouldNotSeeSideNavigation() throws Throwable {
        assertNull(
            sitePage.findElementWithText("Page contents"));
    }

    @Then("^If I inspected the HTML, I should find the \"([^\"]*)\" css class$")
    public void thenIfIInspectedTheHtmlIShouldFindThe(String cssClass) throws Throwable {
        assertNotNull("Can find the CSS class " + cssClass,
            sitePage.findCssClass(cssClass));
    }

    @Then("the page should be in wide mode")
    public void thenPageShouldBeWide() {
        assertTrue(sitePage.isWideMode());
    }

    @Then("the page should not be in wide mode")
    public void thenPageShouldNotBeWide() {
        assertFalse(sitePage.isWideMode());
    }

    @Then("^If I inspected the HTML, I should not find the \"([^\"]*)\" css class$")
    public void thenIfIInspectedTheHtmlIShouldNotFindThe(String cssClass) throws Throwable {
        assertNull("Cannot find the CSS class " + cssClass,
            sitePage.findCssClass(cssClass));
    }

    @Then("I can see the entries footer")
    public void thenICanSeeTheEntriesFooter() {
        assertNotNull("Element is present", sitePage.findCssClass("nhsd-a-box--border-grey"));
    }

    @Then("^I should see (\\d+) (h[1-6]) headers?$")
    public void thenIShouldSeeHeaderCount(int expectedCount, String headerTag) {
        List<WebElement> headers = sitePage.findElementsByTag(headerTag);
        assertThat("Expected number of <" + headerTag + "> tags", headers.size(), is(expectedCount));
    }


    @Then("^all elements with header classes should match their tag levels$")
    public void thenElementsWithHeaderClassesMatchTags() {
        // Define expected tag for each class
        Map<String, String> classToTagMap = new HashMap<>();
        classToTagMap.put("h2","nhsd-t-heading-xl");
        classToTagMap.put("h3","nhsd-t-heading-l");
        classToTagMap.put("h4","nhsd-t-heading-m");
        classToTagMap.put("h5","nhsd-t-heading-s");

        List<String> mismatches = new ArrayList<>();
        
        for (Map.Entry<String, String> entry : classToTagMap.entrySet()) {
            String className = entry.getValue();       // e.g., "nhsd-t-heading-xl"
            String headerTag = entry.getKey();   // e.g., "h2"
    
            // Find all elements that have this class (using CSS selector)
            List<WebElement> elements = sitePage.findElementsByTag(headerTag);

            if (elements.isEmpty()) {
                continue; 
            }
    
            for (WebElement element : elements) {
                String classes = element.getAttribute("class");
    
                if (classes != null && Arrays.asList(classes.split("\\s+")).contains(className)) {
                    mismatches.add(String.format(
                        "Element with tag '%s' should have the class <%s>",
                        headerTag, className
                    ));
                }
            }
        }
        // Fail if there were any mismatches
        if (!mismatches.isEmpty()) {
            throw new AssertionError("Header mismatches found:\n" + String.join("\n", mismatches));
        }
    }
}
