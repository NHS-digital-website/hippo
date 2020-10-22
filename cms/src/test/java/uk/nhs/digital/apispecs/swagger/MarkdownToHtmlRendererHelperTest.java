package uk.nhs.digital.apispecs.swagger;

import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import uk.nhs.digital.apispecs.commonmark.CommonmarkMarkdownConverter;

public class MarkdownToHtmlRendererHelperTest {

    private CommonmarkMarkdownConverter commonmarkMarkdownConverter = mock(CommonmarkMarkdownConverter.class);

    private final MarkdownToHtmlRendererHelper markdownToHtmlRendererHelper
        = new MarkdownToHtmlRendererHelper(commonmarkMarkdownConverter);

    @Test
    public void delegatesToCommonMarkRenderer() {

        // given
        final String markdownToRender = "random text with Markdown elements";
        final String expectedRenderedValue = "random text received from the actual renderer";

        given(commonmarkMarkdownConverter.toHtml(markdownToRender)).willReturn(expectedRenderedValue);

        // when
        final Object actualRenderedValue = markdownToHtmlRendererHelper.apply(
            markdownToRender,
            null // ignored
        );

        // then
        assertThat("Returns value received from the actual renderer.",
            actualRenderedValue,
            sameInstance(expectedRenderedValue)
        );
    }
}