package uk.nhs.digital.ps.test.acceptance.pages.site.ps;

import static uk.nhs.digital.ps.test.acceptance.pages.site.ps.PublicationPageElements.FieldKeys.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublicationPageElements implements PageElements {

    private static final Map<String, By> pageElements = new HashMap<String, By>() {
        {
            put(PUBLICATION_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("title") + "]"));
            put(NOMINAL_PUBLICATION_DATE,
                By.xpath("//*[" + getDataUiPathXpath("nominal-publication-date") + "]"));
            put(UPCOMING_DISCLAIMER,
                By.xpath("//*[" + getDataUiPathXpath("upcoming-disclaimer") + "]"));
            put(SUMMARY,
                By.xpath("//*[" + getDataUiPathXpath("summary") + "]"));
            put(GEOGRAPHIC_COVERAGE,
                By.xpath("//*[" + getDataUiPathXpath("geographic-coverage") + "]"));
            put(GRANULARITY,
                By.xpath("//*[" + getDataUiPathXpath("granularity") + "]"));
            put(DATE_RANGE,
                By.xpath("//*[" + getDataUiPathXpath("date-range") + "]"));
            put(INFORMATION_TYPES,
                By.xpath("//*[" + getDataUiPathXpath("information-types") + "]"));
            put(KEY_FACTS,
                By.xpath("//*[" + getDataUiPathXpath("key-facts") + "]"));
            put(KEY_FACT_IMAGES,
                By.xpath("//*[" + getDataUiPathXpath("key-fact-images") + "]"));
            put(RESOURCES,
                By.xpath("//*[" + getDataUiPathXpath("resources") + "]"));
            put(DATA_SETS,
                By.xpath("//*[" + getDataUiPathXpath("datasets") + "]"));
            put(RELATED_LINKS,
                By.xpath("//*[" + getDataUiPathXpath("related-links") + "]"));
            put(ADMINISTRATIVE_SOURCES,
                By.xpath("//*[" + getDataUiPathXpath("administrative-sources") + "]"));
            put(PAGE_BODY,
                By.xpath("//*[" + getDataUiPathXpath("body") + "]"));
            put(PAGES,
                By.xpath("//*[" + getDataUiPathXpath("pages") + "]"));
            put(PAGE_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("page-title") + "]"));
        }
    };

    public static By getFieldSelector(final String fieldSelectorKey) {
        return pageElements.get(fieldSelectorKey);
    }

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='ps.publication." + fieldName + "'";
    }

    @Override
    public boolean contains(String elementName) {
        return pageElements.containsKey(elementName);
    }

    @Override
    public WebElement getElementByName(String elementName, PageHelper helper) {
        return getElementByName(elementName, 0, helper);
    }

    public WebElement getElementByName(String elementName, int nth, PageHelper helper) {
        List<WebElement> elements = helper.findOptionalElements(pageElements.get(elementName));

        if (elements.size() == 0) {
            return null;
        }

        return elements.get(nth);
    }

    interface FieldKeys {

        String PUBLICATION_TITLE = "Publication Title";
        String NOMINAL_PUBLICATION_DATE = "Publication Date";
        String UPCOMING_DISCLAIMER = "Upcoming Disclaimer";
        String SUMMARY = "Publication Summary";
        String GEOGRAPHIC_COVERAGE = "Publication Geographic Coverage";
        String GRANULARITY = "Publication Granularity";
        String DATE_RANGE = "Publication Date Range";
        String INFORMATION_TYPES = "Publication Information Types";
        String KEY_FACTS = "Publication Key Facts";
        String KEY_FACT_IMAGES = "Publication Key Fact Images";
        String RESOURCES = "Publication Resources";
        String DATA_SETS = "Publication Data Sets";
        String RELATED_LINKS = "Publication Related Links";
        String ADMINISTRATIVE_SOURCES = "Publication Administrative Sources";
        String PAGE_BODY = "Publication Page Body";
        String PAGES = "Publication Pages";
        String PAGE_TITLE = "Publication Page Title";
    }
}
