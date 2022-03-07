package uk.nhs.digital.common.components.apispecification.handlebars.schema;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.Test;

import java.time.Instant;
import java.util.Date;

public class TypeAnySanitisingHelperTest {

    private final TypeAnySanitisingHelper helper = TypeAnySanitisingHelper.INSTANCE;

    @Test
    public void rendersDate_asFullDate() {
        // full-date as per https://tools.ietf.org/html/rfc3339#section-5.6

        // given
        final Date date = Date.from(Instant.parse("2020-02-29T11:12:13Z"));

        // when
        final String actualRendering = helper.apply(date, null);

        // then
        assertThat("Date object is rendered as full-date",
            actualRendering,
            is("2020-02-29")
        );
    }

    @Test
    public void rendersOtherValues_asString() {

        // given
        final String expectedRendering = "non-date model";

        final Object nonDateModel = new Object() {
            @Override public String toString() {
                return expectedRendering;
            }
        };

        // when
        final String actualRendering = helper.apply(nonDateModel, null);

        // then
        assertThat("Date object is rendered as full-date",
            actualRendering,
            is(expectedRendering)
        );
    }
}