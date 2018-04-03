package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.XpathSelectors;

public class GeographicCoverageCmsWidget {

    /**
     * Targets 'parent' div that _contains_ an 'h3/span' child elements, where the {@code <span>} has text 'Geographic Coverage'
     * this is so that further searches can be performed in context of the root element.
     */
    private static final String ROOT_ELEMENT_XPATH = XpathSelectors.EDITOR_BODY
        + "//span[text()='Geographic Coverage']/ancestor::div[contains(@class, 'hippo-editor-field')]";

    /**
     * Targets drop-down's {@code <span class="multiselect-checkboxes">} element.
     */
    private static final String CHECKBOXES_XPATH = ROOT_ELEMENT_XPATH + "//span[contains(@class, 'multiselect-checkboxes')]";

    private final PageHelper helper;
    private final WebDriver webDriver;

    public GeographicCoverageCmsWidget(final PageHelper helper, final WebDriver webDriver) {
        this.helper = helper;
        this.webDriver = webDriver;
    }

    public void selectCheckbox(String value) {
        WebElement checkbox = findCheckbox(value);

        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    public void deselectCheckbox(String value) {
        WebElement checkbox = findCheckbox(value);

        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }

    private WebElement findCheckbox(String value) {
        return helper.findElement(By.xpath(CHECKBOXES_XPATH + "//input[@value = '" + value + "']"));
    }

    private WebElement findRootElement() {
        return helper.findElement(By.xpath(ROOT_ELEMENT_XPATH));
    }

    private WebDriver getWebDriver() {
        return webDriver;
    }

}
