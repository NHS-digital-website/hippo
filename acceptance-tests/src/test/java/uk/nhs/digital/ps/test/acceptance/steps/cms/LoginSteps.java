package uk.nhs.digital.ps.test.acceptance.steps.cms;

import static java.util.concurrent.TimeUnit.MINUTES;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.slf4j.LoggerFactory.getLogger;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import uk.nhs.digital.ps.test.acceptance.data.WebDriversRepo;
import uk.nhs.digital.ps.test.acceptance.pages.DashboardPage;
import uk.nhs.digital.ps.test.acceptance.pages.LoginPage;
import uk.nhs.digital.ps.test.acceptance.steps.AbstractSpringSteps;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class LoginSteps extends AbstractSpringSteps {

    private static final Logger log = getLogger(LoginSteps.class);

    @Autowired
    private LoginPage loginPage;

    @Autowired
    private DashboardPage dashboardPage;

    @Autowired
    private WebDriversRepo webDriversRepo;

    @Given("^I am on login page$")
    public void givenIAmOnLoginPage() throws Throwable {
        loginPage.open();
    }

    @Then("^I (?:can )?open the dashboard page$")
    public void thenICanOpenDashboardPage() throws Throwable {
        assertThat("I should be able to open dashboard page.", dashboardPage.open(), is(true));
    }

    @When("^I submit invalid credentials$")
    public void whenISubmitInvalidCredentials() throws Throwable {
        loginPage.loginWith("admin","wrongpassword");
    }

    @Then("^A login error is displayed$")
    public void thenLoginErrorIsDisplayed() throws Throwable {
        assertThat("A login error is displayed after incorrect login.", loginPage.findLoginErrorMessage(),
            is("Incorrect username or password. Please try again."));
    }

    @Then("^I stay on the login page$")
    public void thenIStayOnTheLoginPage() throws Throwable {
        assertThat("Current page should be login page", loginPage.isOpen(),is(true) );
    }

    public void loginAsCiEditor() throws Throwable {
        loginAs("ci-editor");
    }

    public void loginAs(String user) throws Throwable {
        givenIAmOnLoginPage();
        loginPage.loginWith(user, user);
        assertTrue("Not logged in", loginPage.isLoggedIn());
    }

    @When("^I change my password to \"([^\"]*)\"$")
    public void whenIChangeMyPasswordTo(String password) throws Throwable {
        dashboardPage.open();
        dashboardPage.changePasswordTo(password);
    }

    @Then("^I can see the password error messages$")
    public void thenICanSeeThePasswordErrorMessages() throws Throwable {
        assertThat("Password error is displayed", dashboardPage.getPasswordErrorMessages(),
            hasItems(
                equalTo("Password must be at least 12 characters long"),
                equalTo("Password may not be the same as previous 5 passwords"),
                equalTo("Password must not contain user name, first name or last name"),
                containsString("Password should contain at least one capitalized letter"),
                containsString("Password should contain at least one lower case letter"),
                containsString("Password should contain at least one digit")
            )
        );
    }

    // Performance tests with multiple concurrent users ============================================

    @Given("^(\\d+) '(.+-)' users are logged in$")
    public void givenXUsersAreLoggedIntoTheSystem(final int concurrentUsersCount,
                                                  final String userNamePrefix
    ) throws Throwable {

        final ExecutorService executorService = Executors.newWorkStealingPool(2);

        final String userPassword = "TestPassword0";

        IntStream.rangeClosed(1, concurrentUsersCount).forEach(i -> {

            final String userName = userNamePrefix + i;

            executorService.submit(() -> {

                final WebDriver webDriver = loginPage.loginWithNewSession(userName, userPassword);

                webDriversRepo.addWebDriver(webDriver);
            });
        });

        stopThreadPool(executorService);
    }

    private void stopThreadPool(ExecutorService executorService) throws InterruptedException {
        executorService.shutdown();
        executorService.awaitTermination(5, MINUTES);
    }
}
