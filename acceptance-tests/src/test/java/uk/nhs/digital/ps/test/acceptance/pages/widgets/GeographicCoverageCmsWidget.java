package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import uk.nhs.digital.ps.test.acceptance.models.GeographicCoverage;
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
     * Targets drop-down's {@code <select>} element.
     */
    private static final String DROPDOWN_XPATH = ROOT_ELEMENT_XPATH + "//select[contains(@class, 'dropdown-plugin')]";

    private final PageHelper helper;
    private final WebDriver webDriver;

    public GeographicCoverageCmsWidget(final PageHelper helper, final WebDriver webDriver) {
        this.helper = helper;
        this.webDriver = webDriver;
    }

    public void addGeographicCoverageField() {
        // get element
        WebElement addButton = helper.findElement(By.xpath(ROOT_ELEMENT_XPATH + "//a[contains(@class, 'add-link')]"));

        // scroll to element, to prevent errors like "Other element would receive the click"
        new Actions(webDriver)
            .moveToElement(addButton)
            .moveByOffset(0, 200)
            .perform();

        // click
        addButton.click();

        // wait after click for the dropdown to appear
        helper.findElement(By.xpath(DROPDOWN_XPATH));
    }

    public void populateGeographicCoverageField(final GeographicCoverage geographicCoverage) {
        findDropDown().selectByVisibleText(geographicCoverage.getDisplayValue());
    }

    private Select findDropDown() {
        return new Select(helper.findElement(By.xpath(DROPDOWN_XPATH)));
    }

    private WebElement findRootElement() {
        return helper.findElement(By.xpath(ROOT_ELEMENT_XPATH));
    }

    private WebDriver getWebDriver() {
        return webDriver;
    }

}
