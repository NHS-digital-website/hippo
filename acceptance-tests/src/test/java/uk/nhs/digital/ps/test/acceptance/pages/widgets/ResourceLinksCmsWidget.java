package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.XpathSelectors;

public class ResourceLinksCmsWidget {

    private final PageHelper helper;
    private final WebDriver webDriver;

    public ResourceLinksCmsWidget(final PageHelper helper, final WebDriver webDriver) {
        this.helper = helper;
        this.webDriver = webDriver;
    }

    public void addResourceLinkField() {
        helper.executeWhenStable(() -> findAddButton().click());
    }

    private WebElement findRootElement() {
        return getWebDriver().findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//div[@class='hippo-editor-field' and h3/span[text() = 'Resource Links']]"));
    }

    private WebElement findAddButton() {
        return helper.findElement(() -> findRootElement().findElement(By.linkText("Add")));
    }

    private WebDriver getWebDriver() {
        return webDriver;
    }
}
