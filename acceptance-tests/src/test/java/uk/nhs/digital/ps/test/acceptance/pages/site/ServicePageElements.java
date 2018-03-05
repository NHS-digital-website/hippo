package uk.nhs.digital.ps.test.acceptance.pages.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServicePageElements implements PageElements {

    private final static Map<String, By> pageElements = new HashMap<String, By>() {{
        put("Summary",
            By.cssSelector("#section-summary>p"));

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

    public WebElement getElementByTag(String elementName, int nth, WebDriver webDriver) {
        List<WebElement> elements = webDriver.findElements(pageElements.get(elementName));

        System.out.println("element name = " + elementName);
        System.out.println("elements " + webDriver.findElements(pageElements.get(elementName)));

        if (elements.size() == 0) {
            return null;
        }

        return elements.get(nth);
    }
}


