package uk.nhs.digital.ps.test.acceptance.pages.site.website;

import static uk.nhs.digital.ps.test.acceptance.pages.site.website.PublishedWorkPageElements.FieldKeys.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PublishedWorkPageElements implements PageElements {

    private static final Map<String, By> pageElements = new HashMap<String, By>() {
        {
            put(PUBLISHED_WORK_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("publishedwork.title") + "]"));
            put(PUBLISHED_WORK_SUMMARY,
                By.xpath("//*[" + getDataUiPathXpath("publishedwork.summary") + "]"));
            put(PUBLISHED_WORK_CHAPTER_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("publishedworkchapter.title") + "]"));
            put(PUBLISHED_WORK_CHAPTER_SUMMARY,
                By.xpath("//*[" + getDataUiPathXpath("publishedworkchapter.summary") + "]"));

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

        String PUBLISHED_WORK_TITLE = "Published Work Title";
        String PUBLISHED_WORK_SUMMARY = "Published Work Summary";

        String PUBLISHED_WORK_CHAPTER_TITLE = "Published Work Chapter Title";
        String PUBLISHED_WORK_CHAPTER_SUMMARY = "Published Work Chapter Summary";
    }
}
