package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class LoginPage extends AbstractCmsPage {

    private PageHelper helper;

    public LoginPage(final WebDriverProvider webDriverProvider, PageHelper helper) {
        super(webDriverProvider);
        this.helper = helper;
    }

    public void open() {
        getWebDriver().get(URL);

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

    private WebElement findLoginButton() {
        return helper.findElement(By.name("p::submit"));
    }

    private WebElement findPasswordField() {
        return helper.findElement(By.name("password"));
    }

    private WebElement findUsernameField() {
        return helper.findElement(By.name("username"));
    }

    public boolean isOpen() {
        return helper.findOptionalElement(By.className("hippo-login-form-buttons")) != null;
    }


    public String findLoginErrorMessage() {

        final WebElement hippoLoginFeedbackPanel = helper.findElement(By.className("hippo-login-feedback"));

        return helper.findChildElement(hippoLoginFeedbackPanel, By.tagName("span")).getText();
    }

    public boolean isLoggedIn(){
        return findLogoutButton() != null;
    }

    private WebElement findLogoutButton() {
        return helper.findOptionalElement(By.className("hippo-logout"));
    }

}
