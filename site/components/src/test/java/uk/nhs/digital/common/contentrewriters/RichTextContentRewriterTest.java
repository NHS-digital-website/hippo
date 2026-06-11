package uk.nhs.digital.common.contentrewriters;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static uk.nhs.digital.common.contentrewriters.RichTextContentRewriter.getHtmlCleaner;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.htmlcleaner.TagNode;
import org.junit.Test;

public class RichTextContentRewriterTest {

    @Test
    public void rewrite_doesNotInjectLegacyGoogleAnalyticsEvents() {
        RichTextContentRewriter rewriter = new RichTextContentRewriter();

        String html = "<p><a href='/pagenotfound'>Internal link</a></p>"
            + "<p><a href='https://example.com' onclick='doSomething()'>External link</a></p>";

        String result = rewriter.rewrite(html, null, null, (Mount) null);

        assertFalse(result.contains("logGoogleAnalyticsEvent"));

        TagNode rootNode = getHtmlCleaner().clean(result);
        TagNode[] links = rootNode.getElementsByName("a", true);

        assertNull(links[0].getAttributeByName("onClick"));
        assertEquals("return vjsu.onKeyUp(event)", links[0].getAttributeByName("onKeyUp"));
        assertEquals("doSomething()", links[1].getAttributeByName("onclick"));
    }

    @Test
    public void rewrite_keepsYoutubeCookieConsentPlaceholder() {
        RichTextContentRewriter rewriter = new RichTextContentRewriter();

        String html = "<p>Video</p><iframe src='https://www.youtube.com/embed/example'>Content added by CKEditor</iframe>";

        String result = rewriter.rewrite(html, null, null, (Mount) null);

        assertFalse(result.contains("Content added by CKEditor"));
        assertEquals(1, getHtmlCleaner().clean(result)
            .getElementsByAttValue("class", "cookieconsent-optout-marketing", true, true).length);
    }

    @Test
    public void rewrite_wrapsFirstAbbreviationInstanceInDefinitionElement() {
        RichTextContentRewriter rewriter = new RichTextContentRewriter();

        String html = "<p><abbr title='National Health Service'>NHS</abbr> "
            + "<abbr title='National Health Service'>NHS</abbr></p>";

        String result = rewriter.rewrite(html, null, null, (Mount) null);

        assertEquals(1, getHtmlCleaner().clean(result).getElementsByName("dfn", true).length);
    }
}
