package uk.nhs.digital.ps.test.acceptance.pages.site;

import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;

public interface PageElements {

    boolean contains(String elementName);

    WebElement getElementByName(String elementName, PageHelper webDriver);

    WebElement getElementByName(String elementName, int nth, PageHelper webDriver);
}
