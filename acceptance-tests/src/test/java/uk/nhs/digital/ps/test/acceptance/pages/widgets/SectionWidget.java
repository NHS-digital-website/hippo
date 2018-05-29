package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public abstract class SectionWidget {

    private final WebElement rootElement;

    public SectionWidget(WebElement rootElement) {
        this.rootElement = rootElement;
    }

    protected abstract String getUiPath();

    protected WebElement findElement(final String uiPathFieldSuffix) {
        List<WebElement> elements = rootElement.findElements(
            By.xpath(".//*[@data-uipath='" + getUiPath() + "." + uiPathFieldSuffix + "']")
        );
        return elements.size() == 1 ? elements.get(0) : null;
    }

    protected WebElement getRootElement() {
        return rootElement;
    }
}
