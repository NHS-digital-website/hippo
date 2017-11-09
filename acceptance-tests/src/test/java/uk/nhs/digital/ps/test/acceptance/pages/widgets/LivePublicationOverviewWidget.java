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

public class LivePublicationOverviewWidget {

    private WebElement rootElement;

    public LivePublicationOverviewWidget(final WebElement rootElement) {
        this.rootElement = rootElement;
    }

    public String getTitle() {
        return findElement("title").getText();
    }

    public String getDate() {
        return findElement("nominal-publication-date").getText();
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

    @Factory
    public static Matcher matchesLivePublication(final Publication publication) {

        final String expectedMessage = format(
            "entry representing live publication with title={0}, date={1}, summary={2}",
            publication.getTitle(),
            publication.getNominalPublicationDate().formattedInRespectToCutOff(),
            publication.getSummaryTruncated()
        );

        return new CustomMatcher(expectedMessage) {

            private String actualTitle;
            private String actualDate;
            private String actualSummary;

            @Override
            public boolean matches(final Object item) {

                if (item instanceof LivePublicationOverviewWidget) {

                    final LivePublicationOverviewWidget widget = (LivePublicationOverviewWidget) item;

                    actualTitle = widget.getTitle();
                    actualDate = widget.getDate();
                    actualSummary = widget.getSummary();

                    return publication.getTitle().equals(actualTitle)
                        && publication.getNominalPublicationDate().formattedInRespectToCutOff().equals(actualDate)
                        && publication.getSummaryTruncated().equals(actualSummary)
                        ;
                } else {
                    return false;
                }
            }

            @Override
            public void describeMismatch(final Object item, final Description description) {

                description.appendText(format("got entry with title={0}, date={1}, summary={2}",
                    actualTitle,
                    actualDate,
                    actualSummary
                ));
            }
        };
    }

    private WebElement findElement(final String uiPathFieldSuffix) {
        return rootElement.findElement(
            By.xpath(".//*[@data-uipath='ps.overview.latest-publications.publication." + uiPathFieldSuffix + "']")
        );
    }
}
