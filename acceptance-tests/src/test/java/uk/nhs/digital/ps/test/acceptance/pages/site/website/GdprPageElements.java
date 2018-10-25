package uk.nhs.digital.ps.test.acceptance.pages.site.website;

import static uk.nhs.digital.ps.test.acceptance.pages.site.website.GdprPageElements.FieldKeys.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GdprPageElements implements PageElements {

    private static final Map<String, By> pageElements = new HashMap<String, By>() {
        {
            put(GDPR_SUMMARY_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("gdprsummary.title") + "]"));
            put(GDPR_SUMMARY_SUMMARY,
                By.xpath("//*[" + getDataUiPathXpath("gdprsummary.summary") + "]"));
            put(GDPR_TRANSPARENCY_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("gdprtransparency.title") + "]"));
            put(GDPR_TRANSPARENCY_SUMMARY,
                By.xpath("//*[" + getDataUiPathXpath("gdprtransparency.summary") + "]"));

        }
    };

    public static By getFieldSelector(final String fieldSelectorKey) {
        return pageElements.get(fieldSelectorKey);
    }

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='website." + fieldName + "'";
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

        String GDPR_SUMMARY_TITLE = "GDPR Summary Title";
        String GDPR_SUMMARY_SUMMARY = "GDPR Summary summary";

        String GDPR_TRANSPARENCY_TITLE = "GDPR Transparency Title";
        String GDPR_TRANSPARENCY_SUMMARY = "GDPR Transparency summary";
    }
}
