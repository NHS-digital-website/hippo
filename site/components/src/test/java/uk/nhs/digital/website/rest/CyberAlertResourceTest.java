package uk.nhs.digital.website.rest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import org.hippoecm.hst.configuration.hosting.Mount;
import org.hippoecm.hst.container.RequestContextProvider;
import org.hippoecm.hst.content.beans.query.HstQuery;
import org.hippoecm.hst.content.beans.query.HstQueryManager;
import org.hippoecm.hst.content.beans.query.HstQueryResult;
import org.hippoecm.hst.content.beans.query.filter.Filter;
import org.hippoecm.hst.content.beans.standard.HippoBeanIterator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedMount;
import org.hippoecm.hst.util.SearchInputParsingUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import uk.nhs.digital.ps.test.util.ReflectionHelper;
import uk.nhs.digital.website.beans.CyberAlert;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriInfo;

public class CyberAlertResourceTest {

    private static final String RAW_THREAT_ID = "ABC-123' or '1'='1";
    private static final String SANITISED_THREAT_ID =
        SearchInputParsingUtils.parse(RAW_THREAT_ID, false);

    private static final String CONTENT_PATH = "/content/documents/corporate-website";
    private static final String NORMALISED_CONTENT_PATH = "content/documents/corporate-website";

    @Mock private HttpServletRequest servletRequest;
    @Mock private HttpServletResponse servletResponse;
    @Mock private UriInfo uriInfo;
    @Mock private HstRequestContext requestContext;
    @Mock private HstQueryManager hstQueryManager;
    @Mock private HstQuery hstQuery;
    @Mock private Filter filter;
    @Mock private HstQueryResult queryResult;
    @Mock private HippoBeanIterator iterator;
    @Mock private CyberAlert cyberAlert;
    @Mock private Session session;
    @Mock private Node rootNode;
    @Mock private Node mountContentNode;
    @Mock private ResolvedMount resolvedMount;
    @Mock private Mount mount;

    private CyberAlertResource cyberAlertResource;

    @Before
    public void setUp() throws Exception {
        openMocks(this);

        cyberAlertResource = spy(new CyberAlertResource());

        ReflectionHelper.callMethod(RequestContextProvider.class, "set", HstRequestContext.class, requestContext);

        when(servletRequest.getParameter("threatid")).thenReturn(RAW_THREAT_ID);
        when(requestContext.getSession()).thenReturn(session);
        when(requestContext.getResolvedMount()).thenReturn(resolvedMount);
        when(resolvedMount.getMount()).thenReturn(mount);
        when(mount.getContentPath()).thenReturn(CONTENT_PATH);
        when(session.getRootNode()).thenReturn(rootNode);
        when(rootNode.getNode(NORMALISED_CONTENT_PATH)).thenReturn(mountContentNode);

        doReturn(hstQueryManager).when(cyberAlertResource).getHstQueryManager(session, requestContext);
        when(hstQueryManager.createQuery(mountContentNode, CyberAlert.class)).thenReturn(hstQuery);
        when(hstQuery.createFilter()).thenReturn(filter);
        when(hstQuery.execute()).thenReturn(queryResult);
        when(queryResult.getHippoBeans()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.nextHippoBean()).thenReturn(cyberAlert);
    }

    @After
    public void tearDown() throws Exception {
        ReflectionHelper.callMethod(RequestContextProvider.class, "set", HstRequestContext.class, null);
    }

    @Test
    public void fetchCyberAlert_sanitisesThreatIdQueryParameterBeforeAddingItToJcrQueryFilter() throws Exception {
        cyberAlertResource.fetchCyberAlert(servletRequest, servletResponse, uriInfo, null);

        verify(filter).addEqualTo("website:threatid", SANITISED_THREAT_ID);
        verify(filter, never()).addEqualTo("website:threatid", RAW_THREAT_ID);
    }
}
