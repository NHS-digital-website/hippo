package uk.nhs.digital.ps.test.acceptance.pages.site;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface PageElements {

    boolean contains(String elementName);

    WebElement getElementByName(String elementName, WebDriver webDriver);
}
