package uk.nhs.digital.ps.test.acceptance.steps.site;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import cucumber.api.DataTable;
import cucumber.api.java.en.Then;
import org.hamcrest.Matcher;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.pages.site.ServicePage;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;

import java.util.List;

public class ServiceSteps extends AbstractSpringSteps {

    @Autowired
    private ServicePage servicePage;


    @Then("^I should see the service page title \"([^\"]+)\"$")
    public void thenIShouldSeePageTitled(String pageTitle) throws Throwable {
        assertThat("I should see page titled.", servicePage.getPageTitle(), is(pageTitle));
    }


    @Then("^I should (?:also )?see following items in service page:?$")
    public void thenIShouldAlsoSee(final DataTable pageSections) throws Throwable {
        String elementName = null;
        for (List<String> elementsContent : pageSections.raw()) {
            elementName = elementsContent.get(0);

            assertThat("Page element " + elementName + " should contain ...",
                servicePage.findPageElement(elementName),
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
