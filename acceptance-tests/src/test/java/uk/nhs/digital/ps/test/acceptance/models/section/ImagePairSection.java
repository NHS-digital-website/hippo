package uk.nhs.digital.ps.test.acceptance.models.section;

import static org.hamcrest.core.IsNull.nullValue;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.ImagePairSectionWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.ImageSectionWidget;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;

import java.util.List;

public class ImagePairSection extends BodySection {

    private final ImageSection first;
    private final ImageSection second;

    public ImagePairSection(ImageSection first, ImageSection second) {
        this.first = first;
        this.second = second;
    }

    public Matcher<? super SectionWidget> getMatcher() {
        return new TypeSafeDiagnosingMatcher<SectionWidget>(ImagePairSectionWidget.class) {
            @Override
            protected boolean matchesSafely(SectionWidget item, Description desc) {
                ImagePairSectionWidget widget = (ImagePairSectionWidget) item;
                List<ImageSectionWidget> imageSectionWidgets = widget.getImageSectionWidgets();
                // pad with nulls for the 2 slots we could have had images in
                while (imageSectionWidgets.size() < 2) {
                    imageSectionWidgets.add(null);
                }

                return compare(first.getMatcher(), imageSectionWidgets.get(0), desc)
                    && compare(second == null ? nullValue() : second.getMatcher(), imageSectionWidgets.get(1), desc);
            }

            @Override
            public void describeTo(Description description) {
                description.appendValue(ImagePairSection.this);
            }
        };
    }
}
