package uk.nhs.digital.ps.test.acceptance.models.section;

import static org.hamcrest.Matchers.notNullValue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.ChartSectionWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;

public class ChartSection extends BodySection {
    private final String title;
    private final String dataFileName;

    public ChartSection(String title, String dataFileName) {
        this.title = title;
        this.dataFileName = dataFileName;
    }

    @Override
    public Matcher<? super SectionWidget> getMatcher() {
        return new TypeSafeDiagnosingMatcher<SectionWidget>(ChartSectionWidget.class) {
            @Override
            protected boolean matchesSafely(SectionWidget item, Description desc) {
                ChartSectionWidget widget = (ChartSectionWidget) item;
                return compare(getTitle(), widget.getTitle(), desc)
                    && compare(notNullValue(), widget.getChartImage(), desc)
                    && compare(getDataFileName(), widget.getDataFileName(), desc);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(ChartSection.this);
            }
        };
    }

    public String getTitle() {
        return title;
    }

    public String getDataFileName() {
        return dataFileName;
    }
}
