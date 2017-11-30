package uk.nhs.digital.ps.test.acceptance.steps.site;

import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.config.AcceptanceTestProperties;
import uk.nhs.digital.ps.test.acceptance.pages.site.SitePage;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;
import uk.nhs.digital.ps.test.acceptance.steps.site.ps.PublicationSteps;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;
import static uk.nhs.digital.ps.test.acceptance.util.FileHelper.waitUntilFileAppears;


public class SiteSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(PublicationSteps.class);

    @Autowired
    private SitePage sitePage;

    @Autowired
    private AcceptanceTestProperties acceptanceTestProperties;

    @Given("^I navigate to (?:the )?\"([^\"]+)\" (?:.* )?page$")
    public void iNavigateToPage(String pageName) throws Throwable {
        sitePage.openByPageName(pageName);
    }

    @When("^I search for \"([^\"]+)\"$")
    public void iSearchFor(String searchTerm) throws Throwable {
        sitePage.searchForTerm(searchTerm);
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

    @Then("^I (?:can )?see \"([^\"]+)\" (?:link|button)$")
    public void iSeeLink(String linkTitle) throws Throwable {
        sitePage.findElementWithTitle(linkTitle);
    }

    @Then("^I should see (?:.* )?page titled \"([^\"]+)\"$")
    public void iShouldSeePageTitled(String pageTitle) throws Throwable {
        assertThat("I should see page titled.", sitePage.getPageTitle(), is(pageTitle));
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

        assertThat("I should find page element: " + pageElementName,
            pageElement, is(notNullValue()));

        for (List<String> elementItem : elementItems.raw()) {
            String expectedItemText = elementItem.get(0);

            assertThat("Page element " + pageElementName + " contain item with text: " + expectedItemText,
                getElementTextList(pageElement),
                hasItem(getMatcherForText(expectedItemText)));
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
        for (String downloadLink : downloadTitles.asList(String.class)) {
            WebElement downloadElement = sitePage.findElementWithTitle(downloadLink);

            assertThat("I can find download link with title: " + downloadLink,
                downloadElement, is(notNullValue()));

            if (acceptanceTestProperties.isHeadlessMode()) {
                // At the moment of writing, there doesn't seem to be any easy way available to force Chromedriver
                // to download files when operating in headless mode. It appears that some functionality has been
                // added to DevTools but it's not obvious how to trigger that from Java so, for now at least,
                // we'll only be testing file download when operating in a full, graphical mode.
                //
                // See bug report at https://bugs.chromium.org/p/chromium/issues/detail?id=696481 and other reports
                // available online.
                log.warn("Not testing file download due to running in a headless mode.");
                return;
            }

            // Trigger file download by click the <a> tag.
            sitePage.clickOnElement(downloadElement);

            final Path downloadedFilePath = Paths.get(acceptanceTestProperties.getDownloadDir().toString(),
                downloadLink);

            waitUntilFileAppears(downloadedFilePath);
        }
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

    private Matcher<String> getMatcherForText(String text) {
        if (text.endsWith(" ...")) {
            return startsWith(text.substring(0, text.length() - 4));
        }

        return is(text);
    }

    private String getElementText(WebElement element) {
        if (element.getTagName().equals("input")) {
            return element.getAttribute("value").toString();
        }

        return element.getText();
    }

    private List<String> getElementTextList(WebElement element) {
        return element.findElements(By.tagName("li")).stream()
            .map(WebElement::getText)
            .collect(toList());
    }
}
