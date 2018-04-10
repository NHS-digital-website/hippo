package uk.nhs.digital.ps.test.acceptance.models.section;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.RelatedLinkSectionWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;

public class RelatedLinkSection extends BodySection {
    private final String linkText;
    private final String linkUrl;

    public RelatedLinkSection(String text, String url) {
        this.linkText = text;
        this.linkUrl = url;
    }

    public String getText() {
        return linkText;
    }

    public String getUrl() {
        return linkUrl;
    }

    public Matcher<? super SectionWidget> getMatcher() {
        return new TypeSafeDiagnosingMatcher<SectionWidget>(RelatedLinkSectionWidget.class) {
            @Override
            protected boolean matchesSafely(SectionWidget item, Description desc) {
                RelatedLinkSectionWidget widget = (RelatedLinkSectionWidget) item;
                return compare(linkText, widget.getLinkText(), desc)
                    && compare(linkUrl, widget.getLinkUrl(), desc);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(RelatedLinkSection.this);
            }
        };
    }
}
