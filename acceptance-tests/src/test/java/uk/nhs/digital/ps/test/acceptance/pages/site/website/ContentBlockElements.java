package uk.nhs.digital.ps.test.acceptance.pages.site.website;

import static uk.nhs.digital.ps.test.acceptance.pages.site.website.ContentBlockElements.FieldKeys.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContentBlockElements implements PageElements {

    private static final Map<String, By> pageElements = new HashMap<String, By>() {
        {
            put(SECTION_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("section.title") + "]"));
            put(SECTION_CONTENT,
                By.xpath("//*[" + getDataUiPathXpath("section.content") + "]"));
            put(EMPHASIS_HEADING,
                By.xpath("//*[" + getDataUiPathXpath("emphasis.heading") + "]"));
            put(EMPHASIS_CONENT,
                By.xpath("//*[" + getDataUiPathXpath("emphasis.content") + "]"));
            put(ICONLIST_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("iconlist.title") + "]"));
            put(ICONLIST_INTRODUCTION,
                By.xpath("//*[" + getDataUiPathXpath("iconlist.introduction") + "]"));
            put(ICONLISTITEM_HEADING,
                By.xpath("//*[" + getDataUiPathXpath("iconlistitem.heading") + "]"));
            put(ICONLISTITEM_DESCRIPTION,
                By.xpath("//*[" + getDataUiPathXpath("iconlistitem.description") + "]"));
            put(GALLERYSECTION_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("gallerysection.title") + "]"));
            put(GALLERYSECTION_DESCRIPTION,
                By.xpath("//*[" + getDataUiPathXpath("gallerysection.description") + "]"));

        }
    };

    public static By getFieldSelector(final String fieldSelectorKey) {
        return pageElements.get(fieldSelectorKey);
    }

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='website.contentblock." + fieldName + "'";
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

        String SECTION_TITLE = "Section Title";
        String SECTION_CONTENT = "Section Content";
        String EMPHASIS_HEADING = "Emphasis Box Heading";
        String EMPHASIS_CONENT = "Emphasis Box Content";
        String ICONLIST_TITLE = "Iconlist Title";
        String ICONLIST_INTRODUCTION = "Iconlist Introduction";
        String ICONLISTITEM_HEADING = "Iconlistitem Heading";
        String ICONLISTITEM_DESCRIPTION = "Iconlistitem Description";
        String GALLERYSECTION_TITLE = "Gallery section Title";
        String GALLERYSECTION_DESCRIPTION = "Gallery section Description";
    }
}
