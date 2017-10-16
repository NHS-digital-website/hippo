package uk.nhs.digital.ps.test.acceptance.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.models.PublicationBuilder;
import uk.nhs.digital.ps.test.acceptance.models.TimeSeries;
import uk.nhs.digital.ps.test.acceptance.models.TimeSeriesBuilder;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static uk.nhs.digital.ps.test.acceptance.models.PublicationBuilder.newPublication;

public class ConsumablePublicationSeriesPage extends AbstractConsumablePage {

    private final PageHelper pageHelper;

    public ConsumablePublicationSeriesPage(final WebDriverProvider webDriverProvider, final PageHelper pageHelper) {
        super(webDriverProvider);
        this.pageHelper = pageHelper;
    }

    public void open(final TimeSeries timeSeries) {
        getWebDriver().get(URL + "/publications/" + timeSeries.getUrlName());
    }

    public String getSeriesTitle() {
        return pageHelper.findElement(By.tagName("h1")).getText();
    }

    public List<String> getPublicationTitles() {
        return getWebDriver().findElements(By.className("publicationWithinSeries")).stream()
            .map(WebElement::getText)
            .collect(toList());
    }
}
