package uk.nhs.digital.ps.test.acceptance.pages;

import static java.util.stream.Collectors.toList;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.List;

public class DashboardPage extends AbstractCmsPage {

    private final PageHelper helper;

    public DashboardPage(final WebDriverProvider webDriverProvider,
                         final PageHelper helper,
                         final String cmsUrl) {
        super(webDriverProvider, cmsUrl);
        this.helper = helper;
    }

    public boolean open() {
        helper.findElement(By.xpath(XpathSelectors.TABBED_PANEL + "//li[contains(@class, 'tab0')]"))
            .click();

        return helper.waitForElementUntil(
            ExpectedConditions.attributeContains(
                By.xpath(XpathSelectors.TABBED_PANEL + "//li[contains(@class, 'tab0')]"), "class", "selected"
            )
        );
    }

    public void changePasswordTo(String password) {
        helper.findElement(By.partialLinkText("Change password")).click();

        helper.findElement(By.name("current-password:widget")).sendKeys("admin");
        helper.findElement(By.name("new-password:widget")).sendKeys(password);
        helper.findElement(By.name("check-password:widget")).sendKeys(password);

        helper.findElement(By.xpath("//input[@type='submit' and @value='Change']")).click();
    }

    public List<String> getPasswordErrorMessages() {
        // block until the error is shown
        helper.findElement(By.className("feedbackPanelERROR"));

        WebElement errorPanel = helper.findElement(By.className("feedbackPanel"));
        return helper.findChildElements(errorPanel, By.tagName("li"))
            .stream()
            .map(WebElement::getText)
            .collect(toList());
    }
}
