package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class GranularitySection {
    private final PageHelper helper;
    private final WebDriver webDriver;

    public GranularitySection(final PageHelper helper, final WebDriver webDriver) {
        this.helper = helper;
        this.webDriver = webDriver;
    }

    public void addGranularityField() {
        helper.executeWhenStable(() -> findAddButton().click());
    }

    private WebElement findRootElement() {
        // find 'parent' div that _contains_ an 'h3/span' child elements, where the <span> has text 'Granularity'
        // this is so that further searches can be performed in context of the root element
        return getWebDriver().findElement(
            By.xpath(XpathSelectors.EDITOR_BODY + "//div[@class='hippo-editor-field' and h3/span[text() = 'Granularity']]"));
    }

    private WebElement findAddButton() {
        return helper.findElement(() -> findRootElement().findElement(By.linkText("Add")));
    }

    private WebDriver getWebDriver() {
        return webDriver;
    }
}
