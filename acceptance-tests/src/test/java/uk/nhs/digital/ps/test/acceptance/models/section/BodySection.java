package uk.nhs.digital.ps.test.acceptance.models.section;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import uk.nhs.digital.ps.test.acceptance.pages.widgets.SectionWidget;

import java.util.Objects;

public abstract class BodySection {

    public abstract Matcher<? super SectionWidget> getMatcher();

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, SHORT_PREFIX_STYLE);
    }

    protected boolean compare(Object o1, Object o2, Description desc) {
        if (!Objects.equals(o1, o2)) {
            desc.appendText(" field didn't match <")
                .appendValue(o1)
                .appendText("> : <")
                .appendValue(o2)
                .appendText(">");

            return false;
        }

        return true;
    }
}
