package uk.nhs.digital.common.contentrewriters;

import static org.junit.Assert.assertEquals;
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
    public void rewriteTest() {

        BrandRefreshContentRewriter rewriter = new BrandRefreshContentRewriter();

        String hmtl = "<h3>SCR for patients</h3><p> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound'>including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p><a href='/pagenotfound'> Read more patient information on SCR</a></p>";
        String expectedHmtl = "<h3>SCR for patients</h3><p class=\"nhsd-t-body-s\"> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound'>including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p class=\"nhsd-t-body-s\"><a href='/pagenotfound'> Read more patient information on SCR</a></p>";

        String result = rewriter.rewrite(hmtl, node,requestContext,targetMount);

        TagNode rootNode = getHtmlCleaner().clean(result);
        TagNode[] paragraphs = rootNode.getElementsByName("p", true);

        TagNode expectedRootNode = getHtmlCleaner().clean(expectedHmtl);
        TagNode[] expectedParagraphs = expectedRootNode.getElementsByName("p", true);

        for (int i = 0; i < paragraphs.length; i++) {
            assertEquals(expectedParagraphs[i].getName(), paragraphs[i].getName()); //p
            assertEquals(expectedParagraphs[i].getAttributes(), paragraphs[i].getAttributes()); //class=nhsd-t-body-s
            assertEquals(expectedParagraphs[i].getText().toString(), expectedParagraphs[i].getText().toString()); //text
        }
    }
}
