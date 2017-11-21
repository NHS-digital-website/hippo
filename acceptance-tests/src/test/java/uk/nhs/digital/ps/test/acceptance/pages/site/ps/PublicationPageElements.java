package uk.nhs.digital.ps.test.acceptance.pages.site.ps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.Map;

import static uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPageElements.FieldKeys.*;

public class PublicationPageElements implements PageElements {

    private final static Map<String, By> pageElements = new HashMap<String, By>() {{
        put(PUBLICATION_TITLE,
            By.xpath("//*[" + getDataUiPathXpath("title") + "]"));
        put(NOMINAL_PUBLICATION_DATE,
            By.xpath("//*[" + getDataUiPathXpath("nominal-publication-date") + "]"));
        put(UPCOMING_DISCLAIMER,
            By.xpath("//*[" + getDataUiPathXpath("upcoming-disclaimer") + "]"));
        put(SUMMARY,
            By.xpath("//p[" + getDataUiPathXpath("summary") + "]"));
        put(GEOGRAPHIC_COVERAGE,
            By.xpath("//*[" + getDataUiPathXpath("geographic-coverage") + "]"));
        put(GRANULARITY,
            By.xpath("//*[" + getDataUiPathXpath("granularity") + "]"));
        put(DATE_RANGE,
            By.xpath("//*[" + getDataUiPathXpath("date-range") + "]"));
        put(TAXONOMY,
            By.xpath("//*[" + getDataUiPathXpath("taxonomy") + "]"));
        put(INFORMATION_TYPES,
            By.xpath("//*[" + getDataUiPathXpath("information-types") + "]"));
        put(KEY_FACTS,
            By.xpath("//*[" + getDataUiPathXpath("key-facts") + "]"));
        put(RESOURCES,
            By.xpath("//*[" + getDataUiPathXpath("resources") + "]"));
        put(ADMINISTRATIVE_SOURCES,
            By.xpath("//*[" + getDataUiPathXpath("administrative-sources") + "]"));
    }};

    @Override
    public boolean contains(String elementName) {
        return pageElements.containsKey(elementName);
    }

    @Override
    public WebElement getElementByName(String elementName, WebDriver webDriver) {
        return webDriver.findElement(pageElements.get(elementName));
    }

    public static By getFieldSelector(final String fieldSelectorKey) {
        return pageElements.get(fieldSelectorKey);
    }

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='ps.publication." + fieldName + "'";
    }

    interface FieldKeys {
        String PUBLICATION_TITLE = "Publication Title";
        String NOMINAL_PUBLICATION_DATE = "Nominal Publication Date";
        String UPCOMING_DISCLAIMER = "Upcoming Disclaimer";
        String SUMMARY = "Publication Summary";
        String GEOGRAPHIC_COVERAGE = "Publication Geographic Coverage";
        String GRANULARITY = "Publication Granularity";
        String DATE_RANGE = "Publication Date Range";
        String TAXONOMY = "Publication Taxonomy";
        String INFORMATION_TYPES = "Publication Information Types";
        String KEY_FACTS = "Publication Key Facts";
        String RESOURCES = "Publication Resources";
        String ADMINISTRATIVE_SOURCES = "Publication Administrative Sources";
    }
}
