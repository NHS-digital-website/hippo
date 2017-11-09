package uk.nhs.digital.ps.test.acceptance.pages.site.ps;

import org.openqa.selenium.By;
import uk.nhs.digital.ps.test.acceptance.pages.site.AbstractSitePage;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.LivePublicationOverviewWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.UpcomingPublicationOverivewWidget;
import uk.nhs.digital.ps.test.acceptance.webdriver.WebDriverProvider;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PublicationsOverviewPage extends AbstractSitePage {

    public PublicationsOverviewPage(final WebDriverProvider webDriverProvider) {
        super(webDriverProvider);
    }

    public List<LivePublicationOverviewWidget> getLatestPublicationsWidgets() {
        return getWebDriver()
            .findElements(By.xpath("//*[@data-uipath='ps.overview.latest-publications.publication']"))
            .stream()
            .map(LivePublicationOverviewWidget::new)
            .collect(toList());
    }

    public List<UpcomingPublicationOverivewWidget> getUpcomingPublicationsWidgets() {
        return getWebDriver()
            .findElements(By.xpath("//*[@data-uipath='ps.overview.upcoming-publications.publication']"))
            .stream()
            .map(UpcomingPublicationOverivewWidget::new)
            .collect(toList());
    }
}
