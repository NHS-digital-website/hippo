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

    private final String XPATH_PUBLICATIONS_LIST = "//ul[@data-uipath='ps.series.publications-list']";
    private final String XPATH_TITLE = "//*[@data-uipath='ps.series.title']";

    public ConsumablePublicationSeriesPage(final WebDriverProvider webDriverProvider, final PageHelper pageHelper) {
        super(webDriverProvider);
        this.pageHelper = pageHelper;
    }

    public void open(final TimeSeries timeSeries) {
        getWebDriver().get(URL + "/publications/" + timeSeries.getUrlName());
    }

    public void openFirstPublication() {
        getWebDriver().get(
            getWebDriver().findElement(By.xpath(XPATH_PUBLICATIONS_LIST + "/li[1]/a")).getAttribute("href").toString()
        );
    }

    public String getSeriesTitle() {
        return pageHelper.findElement(By.xpath(XPATH_TITLE)).getText();
    }

    public List<String> getPublicationTitles() {
        return getWebDriver().findElements(By.xpath(XPATH_PUBLICATIONS_LIST + "/li")).stream()
            .map(WebElement::getText)
            .collect(toList());
    }
}
