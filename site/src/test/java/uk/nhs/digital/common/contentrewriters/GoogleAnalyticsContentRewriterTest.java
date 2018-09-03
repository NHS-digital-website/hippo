package uk.nhs.digital.common.contentrewriters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.jcr.Node;

public class GoogleAnalyticsContentRewriterTest {

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

        GoogleAnalyticsContentRewriter rewriter = new GoogleAnalyticsContentRewriter();

        String hmtl = "<div class='rich-text-content'><h3>SCR for patients</h3><p> If you are registered with a GP practice in England your SCR is created automatically, unless you have opted out. 98% of practices are now using the system. You can talk to your practice about <a href='/pagenotfound'>including additional information</a> to do with long term conditions, care preferences or specific communications needs.</p><p><a href='/pagenotfound'> Read more patient information on SCR</a></p><h3>GP information on creating SCRs and including additional information</h3><p>The SCR is created automatically through clinical systems in GP practices and uploaded to the<a href='/services/spine'>Spine</a>. It will then be updated automatically. When new patients are registered the practice should check they are happy to have an SCR. A <a href='/pagenotfound'>sample letter for new patients</a> is available. Additional information can be added to the SCR, with express patient consent, by the GP. The additional information dataset can be included automatically by changing the patient's consent status.</p><p>From 1 July 2017, the General Medical Services (GMS) contract requires GPs to identify patients with moderate or severe frailty, and promote the inclusion of additional information in the SCRs of those with severe frailty by seeking their consent to add it. NHS Digital have sent a resource pack, <a href='/pagenotfound'>Supporting Guidance for promoting enriched Summary Care Records for patients with frailty</a>, to CCGs, to be distributed to GP practices, containing support and guidance on their new duties and how to include additional information in SCRs.</p><p><a href='https://www.england.nhs.uk/publication/supporting-routine-frailty-identification-and-frailty-through-the-gp-contract-20172018/' onclick='doSomething()'>Read NHS England guidance on the requirements to support frailty in the GMS contract 2017-18</a>.&nbsp;</p><p><a href='/pagenotfound'>Read more on including additional information in the SCR</a></p><h3>Viewing SCRs</h3><p>The SCR can be viewed by health and care staff, and viewing is now being rolled out to community pharmacies. SCRs can be viewed through clinical systems or through the <a href='https://portal.national.ncrs.nhs.uk/portal/'>SCRa web viewer</a>, from a machine logged in to the secure NHS network, using a <a href='/pagenotfound'>smartcard</a> with the appropriate Role Based Access Control codes set.</p><p><a href='/pagenotfound'>Read more on viewing SCR and implementing viewing in your organisation</a></p><p><a href='http://webarchive.nationalarchives.gov.uk/20160921135209/http://systems.digital.nhs.uk/scr/pharmacy' ></a><a href='/pagenotfound'>Read more on SCR in community pharmacies</a></p><h3>Security and the SCR</h3><p>Data within the SCR is protected by secure technology. Users must have a <a href='/pagenotfound'>smartcard</a> with the correct codes set. Each use is recorded. A patient can ask to see the record of who has looked at their SCR,  from the viewing organisation. This is called a 'Subject Access Request'.</p><p>Patient data is protected by strict  <a href='/pagenotfound'>information governance</a> rules and procedures. Each organisation using the SCR has at least one privacy  officer who is responsible for monitoring access and can generate audits and reports.</p><p>A patient can also opt out of having  an SCR by returning a completed <a href='http://webarchive.nationalarchives.gov.uk/20160921135209/http://systems.digital.nhs.uk/scr/library/optout.pdf'>opt-out form</a> to their GP practice.</p></div>";

        String result = rewriter.rewrite(hmtl, node,requestContext,targetMount);

        TagNode rootNode = getHtmlCleaner().clean(result);
        TagNode[] links = rootNode.getElementsByName("a", true);

        String regex = "logGoogleAnalyticsEvent";
        Pattern pattern = Pattern.compile(regex);

        for (TagNode link : links) {
            String onClickEvent = link.getAttributeByName("onClick");
            Matcher matcher = pattern.matcher(onClickEvent);

            assert matcher.find();

            // Correct onKeyUp attribute set
            String onKeyUpEvent = link.getAttributeByName("onKeyUp");
            assertThat("onKeyUp event correct", onKeyUpEvent, is("return vjsu.onKeyUp(event)"));
        }
    }

}
