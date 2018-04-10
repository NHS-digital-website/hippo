package uk.nhs.digital.common.valve;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryManager;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.container.ContainerException;
import org.hippoecm.hst.core.container.ValveContext;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.common.valves.ArticleValve;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ArticleValveTest {

    @Mock private HttpServletRequest request;
    @Mock private HttpServletResponse response;
    @Mock private HstRequestContext hstRequestContext;
    @Mock private ValveContext valveContext;
    @Mock private HttpServletRequest hstRequest;
    @Mock private HstQuery hstQuery;
    @Mock private HstQueryManager queryManager;
    @Mock private HippoBean contentBaseBean;
    @Mock private Filter gossidFilter;
    @Mock private HstQueryResult hstQueryResult;
    @Mock private HstLinkCreator hstLinkCreator;
    @Mock private HippoBeanIterator hippoBeanIterator;
    @Mock private HippoBean nextHippoBean;
    @Mock private HstLink hstLink;
    @Mock private HttpServletResponse httpServletResponse;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        given(valveContext.getServletRequest()).willReturn(request);
        given(valveContext.getServletResponse()).willReturn(response);
        given(valveContext.getRequestContext()).willReturn(hstRequestContext);
        given(hstRequestContext.getServletRequest()).willReturn(hstRequest);
        given(hstRequestContext.getQueryManager()).willReturn(queryManager);
        given(hstRequestContext.getSiteContentBaseBean()).willReturn(contentBaseBean);
        given(queryManager.createQuery(hstRequestContext.getSiteContentBaseBean(),
                "website:service",
                "website:general",
                "website:hub",
                "website:componentlist",
                "publicationsystem:legacypublication")).willReturn(hstQuery);
        given(hstQuery.createFilter()).willReturn(gossidFilter);
        given(hstQuery.execute()).willReturn(hstQueryResult);
        given(hstRequestContext.getHstLinkCreator()).willReturn(hstLinkCreator);
        given(hstQueryResult.getHippoBeans()).willReturn(hippoBeanIterator);
        given(hippoBeanIterator.nextHippoBean()).willReturn(nextHippoBean);
        given(hstLinkCreator.create(nextHippoBean, hstRequestContext)).willReturn(hstLink);
        given(hstRequestContext.getServletResponse()).willReturn(httpServletResponse);
        given(hstLink.toUrlForm(hstRequestContext,false)).willReturn("Mock-Link-path");
    }


    @Test
    public void articleValveInvokeWithIdTest() throws ContainerException, IOException {
        given(hstRequest.getPathInfo()).willReturn("/article/123");
        given(hstQueryResult.getSize()).willReturn(1);
        ArticleValve articleValve = new ArticleValve();
        articleValve.invoke(valveContext);
        then(httpServletResponse).should().sendRedirect("Mock-Link-path");
    }

    @Test
    public void articleValveInvokeWithIdSlashTest() throws ContainerException, IOException {
        given(hstRequest.getPathInfo()).willReturn("/article/123/something");
        given(hstQueryResult.getSize()).willReturn(1);
        ArticleValve articleValve = new ArticleValve();
        articleValve.invoke(valveContext);
        then(httpServletResponse).should().sendRedirect("Mock-Link-path");
    }

    @Test
    public void articleValveInvokeWithInvalidIdTest() throws ContainerException, IOException {
        given(hstRequest.getPathInfo()).willReturn("/article/123");
        given(hstQueryResult.getSize()).willReturn(0);
        ArticleValve articleValve = new ArticleValve();
        articleValve.invoke(valveContext);
        verify(httpServletResponse, never()).sendRedirect("Mock-Link-path");
    }

    @Test
    public void articleValveInvokeWithStringTest() throws ContainerException, IOException {
        given(hstRequest.getPathInfo()).willReturn("/article/mock-article");
        ArticleValve articleValve = new ArticleValve();
        articleValve.invoke(valveContext);
        verify(hstQueryResult, never()).getSize();
        verify(httpServletResponse, never()).sendRedirect("Mock-Link-path");
    }
}
