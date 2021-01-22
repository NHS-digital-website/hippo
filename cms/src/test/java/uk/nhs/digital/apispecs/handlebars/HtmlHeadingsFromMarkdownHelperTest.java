package uk.nhs.digital.apispecs.handlebars;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.apispecs.handlebars.OptionsStub.TEMPLATE_CONTENT_FROM_MAIN_BLOCK;

import com.github.jknack.handlebars.Options;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import uk.nhs.digital.apispecs.handlebars.HtmlHeadingsFromMarkdownHelper.HeadingModel;

import java.io.IOException;

public class HtmlHeadingsFromMarkdownHelperTest {

    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Mock Options.Buffer buffer;

    private HtmlHeadingsFromMarkdownHelper helper = HtmlHeadingsFromMarkdownHelper.INSTANCE;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    @SuppressWarnings("StringConcatenationInsideStringBufferAppend")
    public void rendersMainBlock_forEachLevel2HeadingsFromMarkdown_withHeadingDetails() throws IOException {

        // given
        final OptionsStub options = OptionsStub.with(buffer);

        final String markdownWithEligibleHeadings = ""
            + "## Heading 1 not preceded by a new line character"
            + "\ntext A"
            + "\n## Heading 2 preceded with a single new line character"
            + "\ntext B"
            + "\n\n## Heading 3 preceded with two new line characters"
            + "\ntext C"
            + "\n##Heading 4 with no space after the hash characters"
            + "\ntext D"
            + "\n ##Heading 5 with space before the hash characters"
            + "\ntext E"
            + "\n## Heading 6 with no following text"
            + "\n## heading 7 with no preceding text"
            + "\ntext F ## not heading"
            + "\n## Heading 8 with hash characters within it"
            + "\n## Heading 9 with hash characters at the end"
            ;

        // when
        final Options.Buffer actualBuffer = helper.apply(markdownWithEligibleHeadings, options);

        // then
        assertThat("Invokes main block template with details of extracted headers.",
            options.actualMainBlockTemplateParameters(),
            is(asList(
                HeadingModel.with("heading-1-not-preceded-by-a-new-line-character",      "Heading 1 not preceded by a new line character"),
                HeadingModel.with("heading-2-preceded-with-a-single-new-line-character", "Heading 2 preceded with a single new line character"),
                HeadingModel.with("heading-3-preceded-with-two-new-line-characters",     "Heading 3 preceded with two new line characters"),
                HeadingModel.with("heading-4-with-no-space-after-the-hash-characters",   "Heading 4 with no space after the hash characters"),
                HeadingModel.with("heading-5-with-space-before-the-hash-characters",     "Heading 5 with space before the hash characters"),
                HeadingModel.with("heading-6-with-no-following-text",                    "Heading 6 with no following text"),
                HeadingModel.with("heading-7-with-no-preceding-text",                    "heading 7 with no preceding text"),
                HeadingModel.with("heading-8-with-hash-characters-within-it",            "Heading 8 with hash characters within it"),
                HeadingModel.with("heading-9-with-hash-characters-at-the-end",           "Heading 9 with hash characters at the end")
            ))
        );

        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK + "_1");
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK + "_2");
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK + "_3");
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK + "_4");
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK + "_5");
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK + "_6");
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK + "_7");
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK + "_8");
        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK + "_9");

        assertThat("Returns buffer provided by Handlebars through Options.",
            buffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void rendersNothing_forMarkdownWithNoLevel2Headings() {

        // given
        final OptionsStub options = OptionsStub.with(buffer);

        // when
        final String markdownWithNoEligibleHeadings = "";

        final Options.Buffer actualBuffer = helper.apply(markdownWithNoEligibleHeadings, options);

        // then
        verifyZeroInteractions(buffer);

        assertThat("Invokes main template block with details of extracted headers.",
            options.actualMainBlockTemplateParameters(),
            is(empty())
        );

        assertThat("Returns buffer provided by Handlebars through Options.",
            buffer,
            sameInstance(actualBuffer)
        );
    }

    @Test
    public void ignoresHeadingsOfLevelsOtherThan2() throws IOException {

        // given
        final OptionsStub options = OptionsStub.with(buffer);

        final String markdownWithEligibleHeadings = ""
            + "\n# Heading level 1"
            + "\n## Heading level 2"
            + "\n### Heading level 3"
            + "\n#### Heading level 4"
            + "\n##### Heading level 5"
            + "\n###### Heading level 6"
            ;

        // when
        final Options.Buffer actualBuffer = helper.apply(markdownWithEligibleHeadings, options);

        // then
        assertThat("Invokes main template block with details of only the level 2 heading.",
            options.actualMainBlockTemplateParameters(),
            is(singletonList(
                HeadingModel.with("heading-level-2", "Heading level 2")
            ))
        );

        then(buffer).should().append(TEMPLATE_CONTENT_FROM_MAIN_BLOCK + "_1");

        assertThat("Returns buffer provided by Handlebars through Options.",
            buffer,
            sameInstance(actualBuffer)
        );
    }
}