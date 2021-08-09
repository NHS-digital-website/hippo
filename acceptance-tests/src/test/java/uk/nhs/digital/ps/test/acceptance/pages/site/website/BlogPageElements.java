package uk.nhs.digital.ps.test.acceptance.pages.site.website;

import static uk.nhs.digital.ps.test.acceptance.pages.site.website.BlogPageElements.FieldKeys.*;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlogPageElements implements PageElements {

    private static final Map<String, By> pageElements = new HashMap<String, By>() {
        {
            put(BLOG_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("title") + "]"));
            put(BLOG_SUMMARY,
                By.xpath("//*[" + getDataUiPathXpath("summary") + "]"));
            put(BLOG_SHORT_SUMMARY,
                By.xpath("//*[" + getDataUiPathXpath("shortsummary") + "]"));
            put(DATE_OF_PUBLICATION,
                By.xpath("//*[" + getDataUiPathXpath("date") + "]"));
            put(TAXONOMY_TAGS,
                By.xpath("//*[" + getDataUiPathXpath("topics") + "]"));
            put(CATEGORIES,
                By.xpath("//*[" + getDataUiPathXpath("categories") + "]"));
            put(LEAD_IMAGE_CAPTION,
                By.xpath("//*[" + getDataUiPathXpath("leadimagecaption") + "]"));
            put(LEAD_PARAGRAPH,
                By.xpath("//*[" + getDataUiPathXpath("leadparagraph") + "]"));
            put(BACKSTORY,
                By.xpath("//*[" + getDataUiPathXpath("backstory") + "]"));
            put(CONTACT_US,
                By.xpath("//*[" + getDataUiPathXpath("contactus") + "]"));

        }
    };

    public static By getFieldSelector(final String fieldSelectorKey) {
        return pageElements.get(fieldSelectorKey);
    }

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='website.blog." + fieldName + "'";
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

        String BLOG_TITLE = "Blog Title";
        String BLOG_SUMMARY = "Blog Summary";
        String BLOG_SHORT_SUMMARY = "Blog Short Summary";
        String DATE_OF_PUBLICATION = "Date of Publication";
        String TAXONOMY_TAGS = "Taxonomy Tags";
        String CATEGORIES = "Categories";
        String LEAD_IMAGE_CAPTION = "Lead Image Caption";
        String LEAD_PARAGRAPH = "Lead Paragraph";
        String BACKSTORY = "Backstory";
        String CONTACT_US = "Contact Us";
    }
}
