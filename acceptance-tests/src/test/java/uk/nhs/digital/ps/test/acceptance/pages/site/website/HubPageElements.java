package uk.nhs.digital.ps.test.acceptance.pages.site.website;

import static uk.nhs.digital.ps.test.acceptance.pages.site.website.HubPageElements.FieldKeys.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HubPageElements implements PageElements {

    private static final Map<String, By> pageElements = new HashMap<String, By>() {
        {
            put(HUB_TITLE,
                By.xpath("//*[" + getHubDataUiPathXpath("title") + "]"));
            put(SUMMARY,
                By.xpath("//*[" + getHubDataUiPathXpath("summary") + "]"));
            put(VISUAL_HUB_TITLE,
                By.xpath("//*[" + getVisualHubDataUiPathXpath("title") + "]"));
            put(VISUAL_HUB_SUMMARY,
                By.xpath("//*[" + getVisualHubDataUiPathXpath("summary") + "]"));
        }
    };

    public static By getFieldSelector(final String fieldSelectorKey) {
        return pageElements.get(fieldSelectorKey);
    }

    private static String getHubDataUiPathXpath(String fieldName) {
        return "@data-uipath='website.hub." + fieldName + "'";
    }

    private static String getVisualHubDataUiPathXpath(String fieldName) {
        return "@data-uipath='document." + fieldName + "'";
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

        String HUB_TITLE = "Hub Title";
        String SUMMARY = "Hub Summary";
        String VISUAL_HUB_SUMMARY = "Visual Hub Summary";
        String VISUAL_HUB_TITLE = "Visual Hub Title";
    }
}
