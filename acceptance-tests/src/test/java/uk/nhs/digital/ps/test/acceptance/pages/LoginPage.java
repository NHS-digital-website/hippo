package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

public class LoginPage extends AbstractPage {


    public LoginPage(final WebDriverProvider webDriverProvider) {
        super(webDriverProvider);
    }

    public void open() {
        getWebDriver().get(URL);
    }

    public void loginWith(final String username, final String password) {
        findUsernameField().sendKeys(username);
        findPasswordField().sendKeys(password);
        findLoginButton().click();
    }

    private WebElement findLoginButton() {
        return getWebDriver().findElement(By.name("p::submit"));
    }

    private WebElement findPasswordField() {
        return getWebDriver().findElement(By.name("password"));
    }

    private WebElement findUsernameField() {
        return getWebDriver().findElement(By.name("username"));
    }

    public boolean isOpen() {
        WebElement loginButtonsPanel;

        try {
            loginButtonsPanel = getWebDriver().findElement(By.className("hippo-login-form-buttons"));
        } catch (NoSuchElementException e) {
            loginButtonsPanel= null;
        }

        return loginButtonsPanel != null;
    }


    public String findLoginErrorMessage() {

        final WebElement hippoLoginFeedbackPanel = getWebDriver().findElement(By.className("hippo-login-feedback"));

        return hippoLoginFeedbackPanel.findElement(By.tagName("span")).getText();
    }


}
