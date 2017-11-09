package uk.nhs.digital.ps.test.acceptance.pages.widgets;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import uk.nhs.digital.ps.test.acceptance.models.Publication;

import static java.text.MessageFormat.format;

public class UpcomingPublicationOverivewWidget {

    private WebElement webDriver;

    public UpcomingPublicationOverivewWidget(final WebElement rootElement) {
        this.webDriver = rootElement;
    }

    public String getTitle() {
        return findElement("title").getText();
    }

    public String getDate() {
        return findElement("nominal-publication-date").getText();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("title", getTitle())
            .append("date", getDate())
            .toString();
    }

    @Factory
    public static Matcher matchesUpcomingPublication(final Publication publication) {

        final String expectedMessage = format(
            "entry representing upcoming publication with title={0}, date={1}",
            publication.getTitle(),
            publication.getNominalPublicationDate().formattedInRespectToCutOff()
        );

        return new CustomMatcher(expectedMessage) {

            private String actualDate;
            private String actualTitle;

            @Override
            public boolean matches(final Object item) {

                if (item instanceof UpcomingPublicationOverivewWidget) {

                    final UpcomingPublicationOverivewWidget widget = (UpcomingPublicationOverivewWidget) item;

                    actualTitle = widget.getTitle();
                    actualDate = widget.getDate();

                    return publication.getTitle().equals(actualTitle)
                        && publication.getNominalPublicationDate().formattedInRespectToCutOff().equals(actualDate);
                } else {
                    return false;
                }
            }

            @Override
            public void describeMismatch(final Object item, final Description description) {

                description.appendText(format("got entry with title={0}, date={1}",
                    actualTitle,
                    actualDate
                ));
            }
        };
    }

    private WebElement findElement(final String uiPathFieldSuffix) {
        return webDriver.findElement(
            By.xpath("//*[@data-uipath='ps.overview.upcoming-publications.publication." + uiPathFieldSuffix + "']")
        );
    }
}
