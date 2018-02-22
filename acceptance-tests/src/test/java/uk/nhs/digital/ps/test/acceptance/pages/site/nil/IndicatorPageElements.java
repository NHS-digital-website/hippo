package uk.nhs.digital.ps.test.acceptance.pages.site.nil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndicatorPageElements implements PageElements {

    private final static Map<String, By> pageElements = new HashMap<String, By>() {{
        put("Indicator Title",
            By.xpath("//*[" + getDataUiPathXpath("title") + "]"));
        put("Indicator Summary",
            By.xpath("//p[" + getDataUiPathXpath("summary") + "]"));
    }};

    @Override
    public boolean contains(String elementName) {
        return pageElements.containsKey(elementName);
    }

    @Override
    public WebElement getElementByName(String elementName, WebDriver webDriver) {
        return getElementByName(elementName, 0, webDriver);
    }

    @Override
    public WebElement getElementByName(String elementName, int nth, WebDriver webDriver) {
        List<WebElement> elements = webDriver.findElements(pageElements.get(elementName));

        if (elements.size() == 0) {
            return null;
        }

        return elements.get(nth);
    }

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='ps.indicator." + fieldName + "'";
    }
}
