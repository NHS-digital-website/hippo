package uk.nhs.digital.ps.test.acceptance.models.section;

import static org.hamcrest.Matchers.notNullValue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.ChartSectionWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;

public class ChartSection extends BodySection {
    private final String title;

    public ChartSection(String title) {
        this.title = title;
    }

    @Override
    public Matcher<? super SectionWidget> getMatcher() {
        return new TypeSafeDiagnosingMatcher<SectionWidget>(ChartSectionWidget.class) {
            @Override
            protected boolean matchesSafely(SectionWidget item, Description desc) {
                ChartSectionWidget widget = (ChartSectionWidget) item;
                return compare(getTitle(), widget.getTitle(), desc)
                    && compare(notNullValue(), widget.getChartImage(), desc);
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
}
