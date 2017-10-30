package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RelatedLinksSection {

    private final PageHelper helper;
    private final WebDriver webDriver;

    public RelatedLinksSection(final PageHelper helper, final WebDriver webDriver) {
        this.helper = helper;
        this.webDriver = webDriver;
    }

    public void addRelatedLinkField() {
        helper.executeWhenStable(() -> findAddButton().click());
    }

    private WebElement findRootElement() {
        return getWebDriver().findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//div[@class='hippo-editor-field' and h3/span[text() = 'Related Links']]"));
    }

    private WebElement findAddButton() {
        return helper.findElement(() -> findRootElement().findElement(By.linkText("Add")));
    }

    private WebDriver getWebDriver() {
        return webDriver;
    }
}
