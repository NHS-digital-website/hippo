package uk.nhs.digital.ps.test.acceptance.steps.site;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.startsWith;

import com.microsoft.playwright.*;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.hamcrest.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.playwright.PlaywrightProvider;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;
import uk.nhs.digital.ps.test.acceptance.util.TestContentUrls;

public class PlaywrightSteps extends AbstractSpringSteps {
    @Autowired
    private TestContentUrls urlLookup;

    @Autowired
    private PlaywrightProvider playwrightProvider;

    @Given("^I navigate to the \"([^\"]+)\" with playwright$")
    public void givenINavigateToPage(String pageName) {
        Page page = playwrightProvider.getPage();
        String url = urlLookup.lookupSiteUrl(pageName);
        page.navigate(url);
    }

    @Then("^I should see (?:.* )?page titled \"([^\"]+)\" with playwright$")
    public void thenIShouldSeePageTitle(String pageTitle) {
        Page page = playwrightProvider.getPage();
        String text = page.locator("[data-uipath='document.title']").textContent();
        assertThat(text, is(pageTitle));
    }

    @Then("^I should see (?:.* )?page summary \"([^\"]+)\" with playwright$")
    public void thenIShouldSeePageSummary(String summary) {
        Page page = playwrightProvider.getPage();
        String text = page.locator("[data-uipath='document.summary']").textContent();
        assertThat(text, getMatcherForText(summary));
    }

    private static Matcher<String> getMatcherForText(String text) {
        if (text.endsWith(" ...")) {
            return startsWith(text.substring(0, text.length() - 4));
        }

        return is(text);
    }
}
