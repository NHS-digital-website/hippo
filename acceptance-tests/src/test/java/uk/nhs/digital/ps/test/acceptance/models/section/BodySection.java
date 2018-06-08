package uk.nhs.digital.ps.test.acceptance.models.section;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import static org.hamcrest.CoreMatchers.equalTo;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;

public abstract class BodySection {

    public abstract Matcher<? super SectionWidget> getMatcher();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, SHORT_PREFIX_STYLE);
    }

    protected boolean compare(Object expected, Object actual, Description desc) {
        return compare(equalTo(expected), actual, desc);
    }

    protected boolean compare(Matcher<?> expected, Object actual, Description desc) {
        if (!expected.matches(actual)) {
            desc.appendText("field didn't match ");
            expected.describeTo(desc);
            desc.appendText(" ");
            expected.describeMismatch(actual, desc);
            return false;
        }

        return true;
    }
}
