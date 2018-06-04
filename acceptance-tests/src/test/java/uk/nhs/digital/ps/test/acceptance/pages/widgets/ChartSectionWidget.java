package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import static java.lang.String.format;
import static org.springframework.util.CollectionUtils.isEmpty;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class ChartSectionWidget extends SectionWidget {
    public static final String UIPATH = "ps.publication.chart-section";

    public ChartSectionWidget(final WebElement rootElement) {
        super(rootElement);
    }

    @Override
    protected String getUiPath() {
        return UIPATH;
    }

    public String getTitle() {
        WebElement element = findElement("highcharts-title");
        return element == null ? null : element.getText();
    }

    public WebElement getChartImage() {
        return findElement("highcharts-root");
    }

    public String getDataFileName() {
        return getRootElement().findElement(
            By.xpath("//a[@data-uipath='ps.publication.chart-section.data-file']")
        ).getText().trim();
    }

    @Override
    protected WebElement findElement(String className) {
        List<WebElement> elements = getRootElement().findElements(By.className(className));
        return isEmpty(elements) ? null : elements.get(0);
    }

    @Override
    public String toString() {
        return format("title=%s,chartImage=%s",
            getTitle(),
            getChartImage()
        );
    }
}
