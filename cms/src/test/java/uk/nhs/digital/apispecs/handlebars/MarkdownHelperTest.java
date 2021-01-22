package uk.nhs.digital.apispecs.handlebars;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

import com.github.jknack.handlebars.Options;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uk.nhs.digital.apispecs.commonmark.CommonmarkMarkdownConverter;

public class MarkdownHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    private CommonmarkMarkdownConverter commonmarkMarkdownConverter = mock(CommonmarkMarkdownConverter.class);

    private final MarkdownHelper markdownHelper
        = new MarkdownHelper(commonmarkMarkdownConverter);

    @Test
    public void delegatesToCommonMarkRenderer_withNoCustomParametersSpecified() {

        // given
        final String markdownToRender = randomString("markdownToRender_");
        final String expectedRenderedValue = randomString("renderedOutput_");

        given(commonmarkMarkdownConverter.toHtml(eq(markdownToRender), isNull()))
            .willReturn(expectedRenderedValue);

        final OptionsStub options = OptionsStub.with(OptionsStub.Hash.empty());

        // when
        final Object actualRenderedValue = markdownHelper.apply(
            markdownToRender,
            options
        );

        // then
        assertThat("Returns value received from the actual renderer.",
            actualRenderedValue,
            sameInstance(expectedRenderedValue)
        );
    }

    @Test
    public void delegatesToCommonMarkRenderer_applyingHeadingIdPrefix() {

        // given
        final String markdownToRender = randomString("markdownToRender_");
        final String expectedRenderedValue = randomString("renderedOutput_");

        final String customHeadingIdPrefix = "customPrefix__";
        final String parameterNameHeadingIdPrefix = "headingIdPrefix";

        given(commonmarkMarkdownConverter.toHtml(markdownToRender, customHeadingIdPrefix))
            .willReturn(expectedRenderedValue);

        final OptionsStub options = OptionsStub.with(
            OptionsStub.Hash.of(parameterNameHeadingIdPrefix, customHeadingIdPrefix)
        );

        // when
        final Object actualRenderedValue = markdownHelper.apply(
            markdownToRender,
            options
        );

        // then
        assertThat("Returns value received from the actual renderer.",
            actualRenderedValue,
            sameInstance(expectedRenderedValue)
        );
    }

    @Test
    public void throwsExceptionOnMarkdownRenderingFailure() {

        // given
        final RuntimeException expectedCause = new RuntimeException();

        given(commonmarkMarkdownConverter.toHtml(any(), any()))
            .willThrow(expectedCause);

        final Options options = OptionsStub.empty();

        expectedException.expectMessage("Failed to render markdown.");
        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(sameInstance(expectedCause));

        // when
        markdownHelper.apply("some markdown", options);

        // then

        // expectations set up in 'given' are satisfied
    }
}