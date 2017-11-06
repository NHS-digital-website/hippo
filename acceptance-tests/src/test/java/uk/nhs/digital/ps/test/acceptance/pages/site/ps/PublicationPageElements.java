package uk.nhs.digital.ps.test.acceptance.pages.site.ps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.Map;

import static java.text.MessageFormat.format;

public class PublicationPageElements implements PageElements {

    private final static Map<String, By> pageElements = new HashMap<String, By>() {{
        put("Publication Title",
            By.xpath("//*[" + getDataUiPathXpath("title") + "]"));
        put("Publication Summary",
            By.xpath("//p[" + getDataUiPathXpath("summary") + "]"));
        put("Publication Granularity",
            By.xpath("//*[" + getDataUiPathXpath("granularity") + "]"));
        put("Publication Geographic Coverage",
            By.xpath("//*[" + getDataUiPathXpath("geographic-coverage") + "]"));
    }};

    @Override
    public boolean contains(String elementName) {
        return pageElements.containsKey(elementName);
    }

    @Override
    public WebElement getElementByName(String elementName, WebDriver webDriver) {
        return webDriver.findElement(pageElements.get(elementName));
    }

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='ps.publication." + fieldName + "'";
    }
}
