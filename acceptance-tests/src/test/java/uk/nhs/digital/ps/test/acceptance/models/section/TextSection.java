package uk.nhs.digital.ps.test.acceptance.models.section;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.TextSectionWidget;

public class TextSection extends BodySection {
    private final String heading;
    private final String text;

    public TextSection(String heading, String text) {
        this.heading = heading;
        this.text = text;
    }

    public String getHeading() {
        return heading;
    }

    public String getText() {
        return text;
    }

    public Matcher<? super SectionWidget> getMatcher() {
        return new TypeSafeDiagnosingMatcher<SectionWidget>(TextSectionWidget.class) {
            @Override
            protected boolean matchesSafely(SectionWidget item, Description desc) {
                TextSectionWidget widget = (TextSectionWidget) item;
                return compare(heading, widget.getHeading(), desc)
                    && compare(text, widget.getText(), desc);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(TextSection.this);
            }
        };
    }
}
