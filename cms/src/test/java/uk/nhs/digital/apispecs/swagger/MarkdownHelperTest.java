package uk.nhs.digital.apispecs.swagger;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

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
    public void delegatesToCommonMarkRenderer() {

        // given
        final String markdownToRender = "random text with Markdown elements";
        final String expectedRenderedValue = "random text received from the actual renderer";

        given(commonmarkMarkdownConverter.toHtml(markdownToRender)).willReturn(expectedRenderedValue);

        // when
        final Object actualRenderedValue = markdownHelper.apply(
            markdownToRender,
            null // ignored
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

        given(commonmarkMarkdownConverter.toHtml(any())).willThrow(expectedCause);

        expectedException.expectMessage("Failed to render markdown.");
        expectedException.expect(RuntimeException.class);
        expectedException.expectCause(sameInstance(expectedCause));

        // when
        markdownHelper.apply("some markdown", null);

        // then

        // expectations set up in 'given' are satisfied
    }
}