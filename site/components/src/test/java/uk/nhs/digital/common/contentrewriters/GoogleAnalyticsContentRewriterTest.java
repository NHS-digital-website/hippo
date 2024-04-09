package uk.nhs.digital.common.contentrewriters;

import static org.junit.Assert.assertTrue;
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

public class GoogleAnalyticsContentRewriterTest {

    @Mock
    private Node node;
    @Mock
    private HstRequestContext requestContext;
    @Mock
    private Mount targetMount;
    @Mock
    private HippoFolder hippoBean;
    @Mock
    private ContentBeansTool contentBeansTool;
    @Mock
    private ObjectConverter objectConverter;
    @Mock
    private Object childNode;


    @Before
    public void setUp() throws ObjectBeanManagerException {
        openMocks(this);

        given(requestContext.getContentBeansTool()).willReturn(contentBeansTool);
        given(requestContext.getContentBean()).willReturn(hippoBean);
        given(objectConverter.getObject(hippoBean.getNode(), "content")).willReturn(childNode);
        given(contentBeansTool.getObjectConverter()).willReturn(objectConverter);
    }

    @Test
    public void rewriteTest()
    {
        GoogleAnalyticsContentRewriter rewriter = new GoogleAnalyticsContentRewriter();
        String hmtl = "<div class='rich-text-content'><h3>SCR for patients</h3><a href='http://example.com'>Example Link</a></div>";
        String result = rewriter.rewrite(hmtl, node, requestContext, targetMount);
        TagNode rootNode = getHtmlCleaner().clean(result);

        TagNode[] anchorTags = rootNode.getElementsByName("a", true);
        assertTrue("Test fail: Expected passStatus to be true, but it was false", anchorTags.length > 0);
    }
}
