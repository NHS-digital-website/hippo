package uk.nhs.digital.ps.test.acceptance.steps.cms;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.pages.DashboardPage;
import uk.nhs.digital.ps.test.acceptance.pages.LoginPage;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class LoginSteps extends AbstractSpringSteps {

    @Autowired
    private LoginPage loginPage;

    @Autowired
    private DashboardPage dashboardPage;

    @Given("^I am on login page$")
    public void givenIAmOnLoginPage() throws Throwable {
        loginPage.open();
    }

    @When("^I submit my valid admin credentials$")
    public void whenISubmitMyValidAdminCredentials() throws Throwable {
        loginPage.loginWith("admin", "admin");
    }

    @Then("^I can open dashboard page$")
    public void thenICanOpenDashboardPage() throws Throwable {
        assertThat("I should be able to open dashboard page.", dashboardPage.open(), is(true));
    }

    @When("^I submit invalid credentials$")
    public void whenISubmitInvalidCredentials() throws Throwable {
        loginPage.loginWith("admin","wrongpassword");
    }

    @Then("^an error is displayed$")
    public void thenErrorIsDisplayed() throws Throwable {
        assertThat("A login error is displayed after incorrect login.", loginPage.findLoginErrorMessage(),
            is("Incorrect username or password. Please try again."));
    }

    @Then("^I stay on the login page$")
    public void thenIStayOnTheLoginPage() throws Throwable {
        assertThat("Current page should be login page", loginPage.isOpen(),is(true) );
    }

    @Given("^I am logged in as admin$")
    public void givenIAmLoggedInAsAdmin() throws Throwable {
        givenIAmOnLoginPage();
        whenISubmitMyValidAdminCredentials();
        assertThat("Not logged in",loginPage.isLoggedIn(),is(true));
    }
}
