package uk.nhs.digital.common.components;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.linking.HstLink;
import org.hippoecm.hst.core.linking.HstLinkCreator;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.Session;
import javax.jcr.Value;
import javax.jcr.Workspace;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;
import javax.jcr.query.Row;
import javax.jcr.query.RowIterator;

@RunWith(MockitoJUnitRunner.class)
public class IconLibraryComponentTest {

    private static final String ICON_PATH = "/icons/neutral.svg";

    @Mock private HstRequest request;
    @Mock private HstResponse response;
    @Mock private HstRequestContext requestContext;
    @Mock private HttpServletRequest servletRequest;
    @Mock private Session session;
    @Mock private Workspace workspace;
    @Mock private QueryManager queryManager;
    @Mock private Query query;
    @Mock private QueryResult queryResult;
    @Mock private RowIterator rowIterator;
    @Mock private Row row;
    @Mock private Node node;
    @Mock private Node parentNode;
    @Mock private Node grandparentNode;
    @Mock private Property descriptionProperty;
    @Mock private Value descriptionValue;
    @Mock private HstLinkCreator hstLinkCreator;
    @Mock private HstLink hstLink;

    private IconLibraryComponent component;

    @Before
    public void setUp() throws Exception {
        component = new IconLibraryComponent();

        when(request.getRequestContext()).thenReturn(requestContext);
        when(requestContext.getServletRequest()).thenReturn(servletRequest);
        when(requestContext.getSession()).thenReturn(session);
        when(requestContext.getHstLinkCreator()).thenReturn(hstLinkCreator);
        when(session.getWorkspace()).thenReturn(workspace);
        when(workspace.getQueryManager()).thenReturn(queryManager);
        when(queryManager.createQuery(anyString(), eq("xpath"))).thenReturn(query);
        when(query.execute()).thenReturn(queryResult);
        when(queryResult.getRows()).thenReturn(rowIterator);
        when(rowIterator.hasNext()).thenReturn(true, false);
        when(rowIterator.nextRow()).thenReturn(row);
        when(row.getNode()).thenReturn(node);
        when(hstLinkCreator.create(node, requestContext)).thenReturn(hstLink);
        when(hstLink.getPath()).thenReturn(ICON_PATH);

        when(node.getIdentifier()).thenReturn("id");
        when(node.getParent()).thenReturn(parentNode);
        when(parentNode.hasProperty("hippo:name")).thenReturn(false);
        when(parentNode.getParent()).thenReturn(grandparentNode);
        when(grandparentNode.isNodeType("hippostd:folder")).thenReturn(false);
    }

    @Test
    public void sanitisesIconSearchParameterBeforeFilteringIcons() throws Exception {
        when(servletRequest.getParameter("icon-search")).thenReturn("download[");
        when(node.getName()).thenReturn("neutral.svg");
        when(node.hasProperty("hippogallery:description")).thenReturn(true);
        when(node.getProperty("hippogallery:description")).thenReturn(descriptionProperty);
        when(descriptionProperty.getValue()).thenReturn(descriptionValue);
        when(descriptionValue.getString()).thenReturn("[");

        component.doBeforeRender(request, response);

        assertIconsAttributeSize(0);
    }

    @Test
    public void keepsNormalIconSearchFilteringBehaviour() throws Exception {
        when(servletRequest.getParameter("icon-search")).thenReturn("download");
        when(node.getName()).thenReturn("download.svg");
        when(node.hasProperty("hippogallery:description")).thenReturn(false);

        component.doBeforeRender(request, response);

        assertIconsAttributeSize(1);
    }

    private void assertIconsAttributeSize(final int expectedSize) {
        final ArgumentCaptor<List> iconsCaptor = ArgumentCaptor.forClass(List.class);

        verify(request).setAttribute(eq("icons"), iconsCaptor.capture());
        assertEquals(expectedSize, iconsCaptor.getValue().size());
    }
}
