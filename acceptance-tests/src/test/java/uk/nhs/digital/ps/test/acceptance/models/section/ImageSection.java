package uk.nhs.digital.ps.test.acceptance.models.section;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.ImageSectionWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;
import uk.nhs.digital.ps.test.acceptance.util.TestContentUrls;

public class ImageSection extends BodySection {

    private final TestContentUrls testContentUrls;

    private final String imageName;
    private final String altText;
    private final String caption;
    private final String link;

    public ImageSection(String imageName, String altText, String caption, String link) {
        this.imageName = imageName;
        this.altText = altText;
        this.caption = caption;
        this.link = link;
        testContentUrls = TestContentUrls.instance();
    }

    public ImageSection(String imageName, String altText) {
        this(imageName, altText, null, null);
    }

    public String getSource() {
        return testContentUrls.lookupSiteUrl(imageName);
    }

    public String getAltText() {
        return altText;
    }

    public String getCaption() {
        return caption;
    }

    public String getLink() {
        return link;
    }

    public Matcher<? super SectionWidget> getMatcher() {
        return new TypeSafeDiagnosingMatcher<SectionWidget>(ImageSectionWidget.class) {
            @Override
            protected boolean matchesSafely(SectionWidget item, Description desc) {
                ImageSectionWidget widget = (ImageSectionWidget) item;
                return compare(getSource(), widget.getSource(), desc)
                    && compare(altText, widget.getAltText(), desc)
                    && compare(caption, widget.getCaption(), desc)
                    && compare(link, widget.getLink(), desc);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(ImageSection.this);
            }
        };
    }
}
