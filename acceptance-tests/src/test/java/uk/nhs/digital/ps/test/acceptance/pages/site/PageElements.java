package uk.nhs.digital.ps.test.acceptance.pages.site;

import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;

import java.util.List;

public interface PageElements {

    boolean contains(String elementName);

    List<WebElement> getElementsByName(String elementName, PageHelper webDriver);

    WebElement getElementByName(String elementName, PageHelper webDriver);

    WebElement getElementByName(String elementName, int nth, PageHelper webDriver);
}
