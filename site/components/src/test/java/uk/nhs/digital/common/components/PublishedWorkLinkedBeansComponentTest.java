package uk.nhs.digital.common.components;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.onehippo.cms7.essentials.components.ext.DoBeforeRenderExtension;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.common.earlyaccesskey.EarlyAccessKeyProcessor;
import uk.nhs.digital.website.beans.Publishedwork;
import uk.nhs.digital.website.beans.Publishedworkchapter;

import java.util.NoSuchElementException;
import javax.servlet.http.HttpServletRequest;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ContentBeanUtils.class, PublishedWorkLinkedBeansComponent.class,
    EarlyAccessKeyProcessor.class, RequestContextProvider.class, HstServices.class})
@PowerMockIgnore({"javax.management.*", "com.sun.org.apache.xerces.*", "javax.xml.*", "org.xml.*"})
public class PublishedWorkLinkedBeansComponentTest {

    private static final String LINK_PATH = "chapters/@links";

    @Mock
    private HstRequest hstRequest;

    @Mock
    private HstResponse hstResponse;

    @Mock
    private HstRequestContext requestContext;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private Publishedworkchapter publishedworkchapter;

    @Mock
    private HippoBean siteContentBaseBean;

    @Mock
    private HstQuery linkedBeanQuery;

    @Mock
    private HstQueryResult hstQueryResult;

    @Mock
    private HippoBeanIterator hippoBeanIterator;

    @Mock
    private Publishedwork publishedworkDocument;

    @Mock
    private EarlyAccessKeyProcessor earlyAccessKeyProcessor;

    @Mock
    private ComponentManager componentManager;

    @Mock
    private DoBeforeRenderExtension doBeforeRenderExtension;

    private PublishedWorkLinkedBeansComponent component;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        PowerMockito.mockStatic(HstServices.class);
        PowerMockito.mockStatic(RequestContextProvider.class);
        PowerMockito.when(RequestContextProvider.get()).thenReturn(requestContext);
        PowerMockito.when(HstServices.getComponentManager()).thenReturn(componentManager);
        when(componentManager.getComponent(DoBeforeRenderExtension.class.getName()))
            .thenReturn(doBeforeRenderExtension);

        component = PowerMockito.spy(new PublishedWorkLinkedBeansComponent());
        PowerMockito.doReturn(null).when(component).getComponentParameter(anyString());

        PowerMockito.mockStatic(ContentBeanUtils.class);
        PowerMockito.when(ContentBeanUtils.createIncomingBeansQuery(
            eq(publishedworkchapter),
            eq(siteContentBaseBean),
            eq(LINK_PATH),
            eq(Publishedwork.class),
            eq(false)
        )).thenReturn(linkedBeanQuery);

        PowerMockito.whenNew(EarlyAccessKeyProcessor.class).withNoArguments().thenReturn(earlyAccessKeyProcessor);

        doReturn(LINK_PATH).when(component).getComponentParameter("linkPath");

        when(hstRequest.getRequestContext()).thenReturn(requestContext);
        when(requestContext.getServletRequest()).thenReturn(httpServletRequest);
        when(requestContext.getContentBean(Publishedworkchapter.class)).thenReturn(publishedworkchapter);
        when(requestContext.getSiteContentBaseBean()).thenReturn(siteContentBaseBean);

        when(linkedBeanQuery.execute()).thenReturn(hstQueryResult);
        when(hstQueryResult.getHippoBeans()).thenReturn(hippoBeanIterator);
        when(hippoBeanIterator.hasNext()).thenReturn(true);
        when(hippoBeanIterator.nextHippoBean()).thenReturn(publishedworkDocument);
    }

    @Test
    public void setsLinkedDocumentAttributeAndValidatesEarlyAccessKey() throws Exception {
        component.doBeforeRender(hstRequest, hstResponse);

        verify(linkedBeanQuery).setLimit(1);
        verify(hstRequest).setAttribute("linkeddocuments", hstQueryResult);
        verify(earlyAccessKeyProcessor).checkInvalidEarlyAccessKey(
            publishedworkDocument,
            hstRequest,
            hstResponse,
            httpServletRequest
        );
    }

    @Test
    public void handlesMissingParentPublishedWorkWithoutFailing() throws Exception {
        when(hippoBeanIterator.hasNext()).thenReturn(false);
        when(hippoBeanIterator.nextHippoBean()).thenThrow(new NoSuchElementException("no more beans"));

        component.doBeforeRender(hstRequest, hstResponse);

        verify(hstRequest).setAttribute("linkeddocuments", hstQueryResult);
        verifyNoInteractions(earlyAccessKeyProcessor);
    }
}
