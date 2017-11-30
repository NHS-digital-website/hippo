package uk.nhs.digital.ps.test.acceptance.pages.site.ps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.Map;

public class SeriesPageElements implements PageElements {

    private final static Map<String, By> pageElements = new HashMap<String, By>() {{
        put("Series Title",
            By.xpath("//*[" + getDataUiPathXpath("title") + "]"));
        put("Series Summary",
            By.xpath("//p[" + getDataUiPathXpath("summary") + "]"));
        put("Series Granularity",
            By.xpath("//*[" + getDataUiPathXpath("granularity") + "]"));
        put("Series Geographic Coverage",
            By.xpath("//*[" + getDataUiPathXpath("geographic-coverage") + "]"));
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
        return webDriver.findElements(pageElements.get(elementName)).get(nth);
    }

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='ps.series." + fieldName + "'";
    }
}
