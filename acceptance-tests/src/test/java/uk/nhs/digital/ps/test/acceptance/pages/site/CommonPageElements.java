package uk.nhs.digital.ps.test.acceptance.pages.site;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.HashMap;
import java.util.Map;

public class CommonPageElements implements PageElements {

    private final static Map<String, By> pageElements = new HashMap<String, By>() {{
        put("Search Box",
            By.xpath("//input[@name='query']"));
        put("Pagination page",
            By.xpath("//li[contains(@class, " +
            "'pagination__list__item pagination__list__item--current')]/span"));
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
}
