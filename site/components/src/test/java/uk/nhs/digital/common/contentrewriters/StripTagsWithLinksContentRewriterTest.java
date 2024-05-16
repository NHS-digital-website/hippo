package uk.nhs.digital.common.contentrewriters;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.MockitoAnnotations.openMocks;
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

public class StripTagsWithLinksContentRewriterTest {

    @Mock private Node node;
    @Mock private HstRequestContext requestContext;
    @Mock private Mount targetMount;
    @Mock private HippoFolder hippoBean;
    @Mock private ContentBeansTool contentBeansTool;
    @Mock private ObjectConverter objectConverter;
    @Mock private Object childNode;


    @Before
    public void setUp() throws ObjectBeanManagerException {
        openMocks(this);

        given(requestContext.getContentBeansTool()).willReturn(contentBeansTool);
        given(requestContext.getContentBean()).willReturn(hippoBean);
        given(objectConverter.getObject(hippoBean.getNode(), "content")).willReturn(childNode);
        given(contentBeansTool.getObjectConverter()).willReturn(objectConverter);
    }

    @Test
    public void rewriteTest() {

        StripTagsWithLinksContentRewriter rewriter = new StripTagsWithLinksContentRewriter();

        String html = "<h3>SCR for patients</h3><p> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound'>including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p><a href='/pagenotfound'> Read more patient information on SCR</a></p>";
        String expectedHtml = "SCR for patients If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound' class=\"nhsd-a-link\">including additional information</a> to do with long term conditions, care preferences or specific communications needs.<a href='/pagenotfound' class=\"nhsd-a-link\"> Read more patient information on SCR</a>";

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

        assertFalse(result.contains("<h3>"));
        assertFalse(result.contains("<p>"));
    }
}
