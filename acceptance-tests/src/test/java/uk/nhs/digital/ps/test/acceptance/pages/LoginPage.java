package uk.nhs.digital.ps.test.acceptance.pages;

import static org.slf4j.LoggerFactory.getLogger;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class LoginPage extends AbstractCmsPage {

    private static final Logger log = getLogger(LoginPage.class);

    private static final By USERNAME_FIELD_SELECTOR = By.name("username");
    private static final By PASSWORD_FIELD_SELECTOR = By.name("password");
    private static final By SUBMIT_BUTTON_SELECTOR = By.name("p::submit");
    private static final By LOGOUT_BUTTON_SELECTOR = By.className("hippo-logout");

    private PageHelper helper;

    public LoginPage(final WebDriverProvider webDriverProvider,
                     final PageHelper helper,
                     final String cmsUrl) {
        super(webDriverProvider, cmsUrl);
        this.helper = helper;
    }

    public void open() {
        getWebDriver().get(getUrl());

        if (isLoggedIn()) {
            helper.findElement(By.className("hippo-user-icon")).click();
            findLogoutButton().click();
        }

        findLoginButton();
    }

    public void loginWith(final String username, final String password) {
        findUsernameField().sendKeys(username);
        findPasswordField().sendKeys(password);
        findLoginButton().click();
    }

    /**
     * <p>
     * Logs the user with given credentials, creating a brand new session and
     * returning a WebDriver instance particular to this user.
     * </p><p>
     * <strong>To be only used in tests where it's necessary to simulate traffic from
     * many users simultaneously</strong> such as is in performance tests. In 'normal',
     * single user scenarios keep using {@linkplain #loginWith(String, String)}.
     * </p><p>
     * Each instance of WebDriver represents a separate user session and so all
     * interactions with the UI that should be executed as a given user, should
     * be performed using the returned instance of the WebDriver.
     * </p>
     */
    public WebDriver loginWithNewSession(final String userName,
                                         final String userPassword
    ) {
        log.info("{} logs in", userName);

        final WebDriver webDriver = getNewWebDriver(userName);

        webDriver.get(getUrl());

        // Uncommenting the following two lines will ensure that all requests
        // executed via the current WebDriver will be directed to the node given by
        // the cookie value.
        //
        // webDriver.manage().addCookie(new Cookie("BACKEND", "cms1"));
        // webDriver.get(getUrl());

        findUsernameField(webDriver).sendKeys(userName);
        findPasswordField(webDriver).sendKeys(userPassword);
        findLoginButton(webDriver).click();

        waitForLoginToComplete(webDriver);

        log.info("{} logged in via node '{}'",
            userName,
            webDriver.manage().getCookieNamed("BACKEND")
        );

        return webDriver;
    }

    private WebElement findLoginButton() {
        return helper.findElement(SUBMIT_BUTTON_SELECTOR);
    }

    private WebElement findLoginButton(final WebDriver webDriver) {
        return helper.findElement(webDriver, SUBMIT_BUTTON_SELECTOR);
    }

    private WebElement findPasswordField() {
        return helper.findElement(PASSWORD_FIELD_SELECTOR);
    }

    private WebElement findPasswordField(final WebDriver webDriver) {
        return helper.findElement(webDriver, PASSWORD_FIELD_SELECTOR);
    }

    private WebElement findUsernameField() {
        return helper.findElement(USERNAME_FIELD_SELECTOR);
    }

    private WebElement findUsernameField(final WebDriver webDriver) {
        return helper.findElement(webDriver, USERNAME_FIELD_SELECTOR);
    }

    public boolean isOpen() {
        return helper.findOptionalElement(By.className("hippo-login-form-buttons")) != null;
    }


    public String findLoginErrorMessage() {

        final WebElement hippoLoginFeedbackPanel = helper.findElement(By.className("hippo-login-feedback"));

        return helper.findChildElement(hippoLoginFeedbackPanel, By.tagName("span")).getText();
    }

    private void waitForLoginToComplete(final WebDriver webDriver) {
        helper.waitUntilTrue(webDriver, () -> findLogoutButton(webDriver) != null);
    }

    public boolean isLoggedIn() {
        return findLogoutButton() != null;
    }

    private WebElement findLogoutButton(final WebDriver webDriver) {
        return helper.findOptionalElement(webDriver, LOGOUT_BUTTON_SELECTOR);
    }

    private WebElement findLogoutButton() {
        return helper.findOptionalElement(LOGOUT_BUTTON_SELECTOR);
    }
}
