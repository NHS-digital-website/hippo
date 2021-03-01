package uk.nhs.digital.ps.test.acceptance.pages.site.website;

import static uk.nhs.digital.ps.test.acceptance.pages.site.website.GlossaryPageElements.FieldKeys.GLOSSARY_AZ_NAV;
import static uk.nhs.digital.ps.test.acceptance.pages.site.website.GlossaryPageElements.FieldKeys.GLOSSARY_LIST;
import static uk.nhs.digital.ps.test.acceptance.pages.site.website.GlossaryPageElements.FieldKeys.GLOSSARY_TITLE;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlossaryPageElements implements PageElements {

    private static final Map<String, By> pageElements = new HashMap<String, By>() {
        {
            put(GLOSSARY_TITLE,
                By.xpath("//*[" + getDataUiPathXpath("title") + "]"));
            put(GLOSSARY_LIST,
                By.xpath("//*[" + getDataUiPathXpath("list") + "]"));
            put(GLOSSARY_AZ_NAV,
                By.xpath("//*[" + getDataUiPathXpath("az-nav") + "]"));
        }
    };

    public static By getFieldSelector(final String fieldSelectorKey) {
        return pageElements.get(fieldSelectorKey);
    }

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='website.glossary." + fieldName + "'";
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

        String GLOSSARY_TITLE = "Glossary Title";
        String GLOSSARY_LIST = "Glossary List";
        String GLOSSARY_AZ_NAV = "Alphabetical navigation";
    }
}
