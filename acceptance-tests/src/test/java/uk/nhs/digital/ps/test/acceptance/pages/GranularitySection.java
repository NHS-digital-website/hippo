package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import uk.nhs.digital.ps.test.acceptance.models.Granularity;

public class GranularitySection {

    /**
     * Targets 'parent' div that _contains_ an 'h3/span' child elements, where the <span> has text 'Granularity'
     * this is so that further searches can be performed in context of the root element.
     */
    private static final String ROOT_ELEMENT_XPATH = XpathSelectors.EDITOR_BODY
        + "//div[@class='hippo-editor-field' and h3/span[text() = 'Granularity']]";

    /** Targets drop-down's {@code <select>} element. */
    private static final String DROPDOWN_XPATH = ROOT_ELEMENT_XPATH + "//select[@class='dropdown-plugin']";

    private final PageHelper helper;
    private final WebDriver webDriver;

    public GranularitySection(final PageHelper helper, final WebDriver webDriver) {
        this.helper = helper;
        this.webDriver = webDriver;
    }

    public void addGranularityField() {
        helper.executeWhenStable(() -> findAddButton().click());
    }

    public void populateGranularityField(final Granularity granularity) {
        helper.executeWhenStable(() -> findDropDown().selectByVisibleText(granularity.getDisplayValue()));
    }

    private Select findDropDown() {
        return new Select(helper.findElement(By.xpath(DROPDOWN_XPATH)));
    }

    private WebElement findAddButton() {
        return helper.findElement(() -> findRootElement().findElement(By.linkText("Add")));
    }

    private WebElement findRootElement() {
        return getWebDriver().findElement(By.xpath(ROOT_ELEMENT_XPATH));
    }

    private WebDriver getWebDriver() {
        return webDriver;
    }
}
