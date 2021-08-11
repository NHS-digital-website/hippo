package uk.nhs.digital.ps.test.acceptance.pages.site.website;

import static uk.nhs.digital.ps.test.acceptance.pages.site.website.RoadmapPageElements.FieldKeys.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoadmapPageElements implements PageElements {

    private static final Map<String, By> pageElements = new HashMap<String, By>() {
        {
            put(ROADMAP_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("title") + "]"));
            put(SUMMARY,
                By.xpath("//*[" + getDataUiPathXpath("summary") + "]"));
            put(ROADMAP_ITEM_1,
                By.xpath("//*[" + getDataUiPathXpath("Roadmap Item 1") + "]"));
            put(ROADMAP_ITEM_2,
                By.xpath("//*[" + getDataUiPathXpath("Roadmap Item 2") + "]"));

        }
    };

    public static By getFieldSelector(final String fieldSelectorKey) {
        return pageElements.get(fieldSelectorKey);
    }

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='website.roadmap." + fieldName + "'";
    }

    @Override
    public boolean contains(String elementName) {
        return pageElements.containsKey(elementName);
    }

    public List<WebElement> getElementsByName(String elementName, PageHelper helper) {
        return helper.findOptionalElements(pageElements.get(elementName));
    }

    @Override
    public WebElement getElementByName(String elementName, PageHelper helper) {
        return getElementByName(elementName, 0, helper);
    }

    public WebElement getElementByName(String elementName, int nth, PageHelper helper) {
        List<WebElement> elements = getElementsByName(elementName, helper);

        if (elements.size() == 0) {
            return null;
        }
        return elements.get(nth);
    }

    interface FieldKeys {

        String ROADMAP_TITLE = "Roadmap Title";
        String SUMMARY = "Roadmap Summary";
        String ROADMAP_ITEM_1 = "Roadmap Item 1";
        String ROADMAP_ITEM_2 = "Roadmap Item 2";
    }
}
