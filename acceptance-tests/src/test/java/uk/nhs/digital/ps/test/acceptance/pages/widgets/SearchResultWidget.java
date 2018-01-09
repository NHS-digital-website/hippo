package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SearchResultWidget {

    private WebElement rootElement;

    public SearchResultWidget(final WebElement rootElement) {
        this.rootElement = rootElement;
    }

    public String getTitle() {
        return findElement("title").getText();
    }

    public String getDate() {
        return findElement("date").getText();
    }

    public String getSummary() {
        return findElement("summary").getText();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("title", getTitle())
            .append("date", getDate())
            .append("summary", getSummary())
            .toString();
    }

    private WebElement findElement(final String uiPathFieldSuffix) {
        return rootElement.findElement(
            By.xpath(".//*[@data-uipath='ps.search-results.result." + uiPathFieldSuffix + "']")
        );
    }
}
