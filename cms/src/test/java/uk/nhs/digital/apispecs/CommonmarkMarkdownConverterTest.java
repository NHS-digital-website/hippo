package uk.nhs.digital.apispecs;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.test.util.FileUtils.fileContentFromClasspath;

import org.junit.Before;
import org.junit.Test;
import uk.nhs.digital.apispecs.commonmark.CommonmarkMarkdownConverter;

public class CommonmarkMarkdownConverterTest {

    private static final String TEST_DATA_FILES_PATH =
        "/test-data/api-specifications/CommonmarkMarkdownConverterTest";

    private CommonmarkMarkdownConverter commonmarkMarkdownConverter;

    @Before
    public void setUp() {

        initMocks(this);

        commonmarkMarkdownConverter = new CommonmarkMarkdownConverter();
    }

    @Test
    public void convertMarkdownWithBackticksToHtmlWithCodeTags() {

        // given
        final String mardownWithBackticks = getMarkdownWithBackticksText();
        final String expectedHtml = htmlWithCodeTags();

        // when
        final String actualHtml = commonmarkMarkdownConverter.toHtml(mardownWithBackticks);

        // then
        assertThat(
            "Markdown with backticks converted to HTML with <code> tags",
            actualHtml,
            is(expectedHtml)
        );
    }

    @Test
    public void convertMarkdownToHtmlWithTables() {

        // given
        final String markdown = getTableMarkdown();
        final String expectedHtml = htmlWithTableTags();

        // when
        final String actualHtml = commonmarkMarkdownConverter.toHtml(markdown);

        // then
        assertThat(
            "Markdown with table converted to HTML with <table> tags",
            actualHtml,
            is(expectedHtml)
        );

    }

    private String getTableMarkdown() {
        return fileContentFromClasspath(TEST_DATA_FILES_PATH + "/markdown-with-table.md");
    }

    private String htmlWithTableTags() {
        return fileContentFromClasspath(TEST_DATA_FILES_PATH + "/html-with-table-tags.html");
    }

    private String getMarkdownWithBackticksText() {
        return fileContentFromClasspath(TEST_DATA_FILES_PATH + "/markdown-with-backticks.md");
    }

    private String htmlWithCodeTags() {
        return fileContentFromClasspath(TEST_DATA_FILES_PATH + "/html-with-code-tags.html");
    }
}
