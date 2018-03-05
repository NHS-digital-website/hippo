package uk.nhs.digital.ps.test.acceptance.steps.site;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.pages.site.HomePage;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.slf4j.LoggerFactory.getLogger;

public class HomeSteps extends AbstractSpringSteps {

    private final static Logger log = getLogger(HomeSteps.class);

    @Autowired
    private HomePage homePage;


    @Then("^I should see the home page title \"([^\"]+)\"$")
    public void iShouldSeePageTitled(String pageTitle) throws Throwable {
        assertThat("I should see page titled.", homePage.getPageTitle(), is(pageTitle));
    }


    @Then("^I should (?:also )?see following items in home page:?$")
    public void iShouldAlsoSee(final DataTable pageSections) throws Throwable {
        String elementName = null;
        for (List<String> elementsContent : pageSections.raw()) {
            elementName = elementsContent.get(0);

            assertThat("Page element " + elementName + " should contain ...",
                homePage.findPageElement(elementName),
                getMatcherForText(elementsContent.get(1)));
        }
    }

    private static Matcher<String> getMatcherForText(String text) {
        if (text.endsWith(" ...")) {
            return startsWith(text.substring(0, text.length() - 4));
        }

        return is(text);
    }

}
