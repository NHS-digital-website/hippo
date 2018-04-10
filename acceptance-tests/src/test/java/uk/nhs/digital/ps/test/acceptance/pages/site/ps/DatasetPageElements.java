package uk.nhs.digital.ps.test.acceptance.pages.site.ps;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.pages.PageHelper;
import uk.nhs.digital.ps.test.acceptance.pages.site.PageElements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatasetPageElements implements PageElements {

    private static final Map<String, By> pageElements = new HashMap<String, By>() {
        {
            put("Dataset Title",
                By.xpath("//*[" + getDataUiPathXpath("title") + "]"));
            put("Dataset Summary",
                By.xpath("//*[" + getDataUiPathXpath("summary") + "]"));
            put("Dataset Granularity",
                By.xpath("//*[" + getDataUiPathXpath("granularity") + "]"));
            put("Dataset Geographic Coverage",
                By.xpath("//*[" + getDataUiPathXpath("geographic-coverage") + "]"));
            put("Dataset Date Range",
                By.xpath("//*[" + getDataUiPathXpath("date-range") + "]"));
            put("Dataset Resources",
                By.xpath("//*[" + getDataUiPathXpath("resources") + "]"));
            put("Dataset Nominal Date",
                By.xpath("//*[" + getDataUiPathXpath("nominal-date") + "]"));
            put("Dataset Next Publication Date",
                By.xpath("//*[" + getDataUiPathXpath("next-publication-date") + "]"));
        }
    };

    private static String getDataUiPathXpath(String fieldName) {
        return "@data-uipath='ps.dataset." + fieldName + "'";
    }

    @Override
    public boolean contains(String elementName) {
        return pageElements.containsKey(elementName);
    }

    @Override
    public WebElement getElementByName(String elementName, PageHelper helper) {
        return getElementByName(elementName, 0, helper);
    }

    @Override
    public WebElement getElementByName(String elementName, int nth, PageHelper helper) {
        List<WebElement> elements = helper.findElements(pageElements.get(elementName));

        if (elements.size() == 0) {
            return null;
        }

        return elements.get(nth);
    }

}
