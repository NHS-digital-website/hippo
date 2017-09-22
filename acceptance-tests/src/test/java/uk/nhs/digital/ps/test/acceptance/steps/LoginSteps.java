package uk.nhs.digital.ps.test.acceptance.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.pages.DashboardPage;
import uk.nhs.digital.ps.test.acceptance.pages.LoginPage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LoginSteps extends AbstractSpringSteps {

    @Autowired
    private LoginPage loginPage;

    @Autowired
    private DashboardPage dashboardPage;

    @Given("^I am on login page$")
    public void iAmOnLoginPage() throws Throwable {
        loginPage.open();
    }

    @When("^I submit my valid admin credentials$")
    public void iSubmitMyValidAdminCredentials() throws Throwable {
        loginPage.loginWith("admin", "admin");
    }

    @Then("^a dashboard is displayed$")
    public void aDashboardIsDisplayed() throws Throwable {
        assertThat("Current page is the dashboard page.", dashboardPage.isOpen(), is(true));
    }

    @When("^I submit my invalid credentials$")
    public void iSubmitMyInvalidCredentials() throws Throwable {
        loginPage.loginWith("admin","wrongpassword");
    }

    @Then("^an error is displayed$")
    public void anErrorIsDisplayed() throws Throwable {
        assertThat("A login error is displayed after incorrect login.", loginPage.findLoginErrorMessage(),
            is("Incorrect username or password. Please try again."));
    }

    @Then("^I stay on the login page$")
    public void iStayOnTheLoginPage() throws Throwable {
        assertThat("Current page is login page.", loginPage.isOpen(), is(true));
    }
}
