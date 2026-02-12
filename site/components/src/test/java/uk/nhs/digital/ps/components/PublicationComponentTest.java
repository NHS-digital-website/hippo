package uk.nhs.digital.ps.components;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.content.beans.query.exceptions.QueryException;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.hippoecm.hst.mock.core.component.MockHstResponse;
import org.hippoecm.hst.mock.core.container.MockComponentManager;
import org.hippoecm.hst.mock.core.request.MockComponentConfiguration;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockServletContext;
import uk.nhs.digital.ps.beans.Dataset;
import uk.nhs.digital.ps.beans.ExtAttachment;
import uk.nhs.digital.ps.beans.Publication;
import uk.nhs.digital.ps.beans.RelatedLink;
import uk.nhs.digital.ps.beans.Series;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;
import uk.nhs.digital.ps.site.exceptions.DataRestrictionViolationException;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;
import uk.nhs.digital.website.beans.SupplementaryInformation;

public class PublicationComponentTest extends MockitoSessionTestBase {

    @Mock private Publication publication;

    private PublicationComponent component;
    private MockHstRequest request;
    private MockHstResponse response;
    private MockHstRequestContext requestContext;
    private ComponentManager originalComponentManager;

    @Before
    public void setUp() throws Exception {
        originalComponentManager = HstServices.getComponentManager();
        HstServices.setComponentManager(new MockComponentManager());

        component = new PublicationComponent();
        component.init(new MockServletContext(), new MockComponentConfiguration());

        request = new MockHstRequest();
        response = new MockHstResponse();
        requestContext = new MockHstRequestContext();
        request.setRequestContext(requestContext);
        ModifiableRequestContextProvider.set(requestContext);
    }

    @After
    public void tearDown() {
        ModifiableRequestContextProvider.set(null);
        HstServices.setComponentManager(originalComponentManager);
    }

    @Test
    public void publicPublicationPopulatesRequestAttributes() throws HstComponentException, QueryException {
        when(publication.isPubliclyAccessible()).thenReturn(true);
        when(publication.getSummary()).thenReturn(new StructuredText("Summary"));
        when(publication.getSections()).thenReturn(singletonList(mock(HippoBean.class)));
        when(publication.getKeyFacts()).thenReturn(new StructuredText("facts"));
        Series series = mock(Series.class);
        when(series.getAdministrativeSources()).thenReturn("admin sources");
        when(publication.getParentSeriesCollectionDocument()).thenReturn(series);
        when(publication.getDatasets()).thenReturn(singletonList(mock(Dataset.class)));
        when(publication.getAttachments()).thenReturn(singletonList(mock(ExtAttachment.class)));
        when(publication.getResourceLinks()).thenReturn(singletonList(mock(RelatedLink.class)));
        when(publication.getSupplementaryInformation()).thenReturn(singletonList(mock(SupplementaryInformation.class)));
        when(publication.getKeyFactImages()).thenReturn(emptyList());
        requestContext.setContentBean(publication);

        component.doBeforeRender(request, response);

        assertSame(publication, request.getAttribute("publication"));
        assertEquals(
            asList(
                "Summary",
                "Highlights",
                "Key facts",
                "Administrative sources",
                "Data sets",
                "Resources",
                "Supplementary information requests"
            ),
            request.getAttribute("index")
        );
        assertEquals(emptyList(), request.getAttribute("keyFactImageSections"));
    }

    @Test
    public void nonPublicationBeanIsIgnored() throws HstComponentException {
        requestContext.setContentBean(mock(HippoBean.class));

        component.doBeforeRender(request, response);

        assertNull(request.getAttribute("publication"));
        assertNull(request.getAttribute("index"));
        assertNull(request.getAttribute("keyFactImageSections"));
    }

    @Test
    public void embargoedPublicationDoesNotAccessRestrictedSections() throws HstComponentException {
        when(publication.isPubliclyAccessible()).thenReturn(false);
        when(publication.getKeyFactImages()).thenThrow(new DataRestrictionViolationException("restricted"));
        requestContext.setContentBean(publication);

        component.doBeforeRender(request, response);

        assertSame(publication, request.getAttribute("publication"));
        assertEquals(emptyList(), request.getAttribute("index"));
        assertEquals(emptyList(), request.getAttribute("keyFactImageSections"));
        verify(publication, never()).getKeyFactImages();
    }
}
