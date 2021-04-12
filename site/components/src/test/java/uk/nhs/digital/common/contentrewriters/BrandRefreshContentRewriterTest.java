package uk.nhs.digital.common.contentrewriters;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.common.contentrewriters.GoogleAnalyticsContentRewriter.getHtmlCleaner;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.content.beans.ObjectBeanManagerException;
import org.hippoecm.hst.content.beans.manager.ObjectConverter;
import org.hippoecm.hst.content.beans.standard.HippoFolder;
import org.hippoecm.hst.content.tool.ContentBeansTool;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.htmlcleaner.TagNode;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import javax.jcr.Node;

public class BrandRefreshContentRewriterTest {

    @Mock private Node node;
    @Mock private HstRequestContext requestContext;
    @Mock private Mount targetMount;
    @Mock private HippoFolder hippoBean;
    @Mock private ContentBeansTool contentBeansTool;
    @Mock private ObjectConverter objectConverter;
    @Mock private Object childNode;


    @Before
    public void setUp() throws ObjectBeanManagerException {
        initMocks(this);

        given(requestContext.getContentBeansTool()).willReturn(contentBeansTool);
        given(requestContext.getContentBean()).willReturn(hippoBean);
        given(objectConverter.getObject(hippoBean.getNode(), "content")).willReturn(childNode);
        given(contentBeansTool.getObjectConverter()).willReturn(objectConverter);
    }

    @Test
    public void rewriteTestForPTag() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h3>SCR for patients</h3><p> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound'>including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p><a href='/pagenotfound'> Read more patient information on SCR</a></p>";
        String expectedHtml = "<h3>SCR for patients</h3><p class=\"nhsd-t-body\"> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound'>including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p class=\"nhsd-t-body\"><a href='/pagenotfound'> Read more patient information on SCR</a></p>";

        String result = rewriter.rewrite(html, node,requestContext,targetMount);

        TagNode rootNode = getHtmlCleaner().clean(result);
        TagNode[] paragraphs = rootNode.getElementsByName("p", true);

        TagNode expectedRootNode = getHtmlCleaner().clean(expectedHtml);
        TagNode[] expectedParagraphs = expectedRootNode.getElementsByName("p", true);

        for (int i = 0; i < paragraphs.length; i++) {
            assertEquals(expectedParagraphs[i].getName(), paragraphs[i].getName()); //p
            assertEquals(expectedParagraphs[i].getAttributeByName("class"), paragraphs[i].getAttributeByName("class")); //nhsd-t-body
            assertEquals(expectedParagraphs[i].getText().toString(), paragraphs[i].getText().toString()); //text
        }
    }

    @Test
    public void rewriteTestForATag() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String html = "<h3>SCR for patients</h3><p> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound'>including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p><a href='/pagenotfound'> Read more patient information on SCR</a></p>";
        String expectedHtml = "<h3>SCR for patients</h3><p> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound' class=\"nhsd-a-link\">including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p><a href='/pagenotfound' class=\"nhsd-a-link\"> Read more patient information on SCR</a></p>";

        String result = rewriter.rewrite(html, node,requestContext,targetMount);

        TagNode rootNode = getHtmlCleaner().clean(result);
        TagNode[] links = rootNode.getElementsByName("a", true);

        TagNode expectedRootNode = getHtmlCleaner().clean(expectedHtml);
        TagNode[] expectedLinks = expectedRootNode.getElementsByName("a", true);

        for (int i = 0; i < links.length; i++) {
            assertEquals(expectedLinks[i].getName(), links[i].getName()); //a
            assertEquals(expectedLinks[i].getAttributeByName("class"), links[i].getAttributeByName("class")); //nhsd-a-link
            assertEquals(expectedLinks[i].getText().toString(), links[i].getText().toString()); //text
        }
    }

    @Test
    public void rewriteTestForImgTag() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        // Sample img tag for rich text (taken from section compound)
        String html = "<h1>Image</h1><img src=\"https://en.wikipedia.org/wiki/NHS_Digital#/media/File:NHS_Digital_logo.svg\" alt=\"This is the alt txt\" align=\"top\">";

        String result = rewriter.rewrite(html, node, requestContext, targetMount);

        assertTrue(result.contains("img"));
        assertTrue(result.contains("alt"));
        assertTrue(result.contains("align"));

        TagNode rootNode = getHtmlCleaner().clean(result);
        TagNode image = rootNode.findElementByName("img", true);

        assertEquals("This is the alt txt", image.getAttributeByName("alt"));
        assertEquals("top", image.getAttributeByName("align"));
    }
}
