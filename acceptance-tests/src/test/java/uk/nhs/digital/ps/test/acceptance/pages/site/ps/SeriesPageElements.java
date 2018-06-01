package uk.nhs.digital.ps.test.acceptance.pages.site.ps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeriesPageElements implements PageElements {

    private static final Map<String, By> pageElements = new HashMap<String, By>() {
        {
            put("Series Title",
                By.xpath("//*[" + getDataUiPathXpath("title") + "]"));
            put("Series Summary",
                By.xpath("//p[" + getDataUiPathXpath("summary") + "]"));
            put("Series Granularity",
                By.xpath("//*[" + getDataUiPathXpath("granularity") + "]"));
            put("Series Geographic Coverage",
                By.xpath("//*[" + getDataUiPathXpath("geographic-coverage") + "]"));
            put("Series Latest Publication",
                By.xpath("//*[" + getDataUiPathXpath("publications-list.latest") + "]"));
            put("Series Previous Publications",
                By.xpath("//*[" + getDataUiPathXpath("publications-list.previous") + "]"));
            put("Series Upcoming Publications",
                By.xpath("//*[" + getDataUiPathXpath("publications-list.upcoming") + "]"));
            put("Series Publications",
                By.xpath("//*[" + getDataUiPathXpath("publications-list") + "]"));
            put("Series Resources",
                By.xpath("//*[" + getDataUiPathXpath("resources") + "]"));
        }
    };

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='ps.series." + fieldName + "'";
    }

    @Override
    public boolean contains(String elementName) {
        return pageElements.containsKey(elementName);
    }

    @Override
    public WebElement getElementByName(String elementName, PageHelper helper) {
        return getElementByName(elementName, 0, helper);
    }

    @Override
    public WebElement getElementByName(String elementName, int nth, PageHelper helper) {
        List<WebElement> elements = helper.findElements(pageElements.get(elementName));

        if (elements.size() == 0) {
            return null;
        }

        return elements.get(nth);
    }
}
