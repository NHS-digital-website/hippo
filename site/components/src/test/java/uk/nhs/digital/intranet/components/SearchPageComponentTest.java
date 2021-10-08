package uk.nhs.digital.intranet.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static uk.nhs.digital.intranet.components.SearchPageComponent.REQUEST_ATTR_AREA;
import static uk.nhs.digital.intranet.components.SearchPageComponent.REQUEST_ATTR_PAGEABLE;

import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstParameterInfoProxyFactoryImpl;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.hippoecm.hst.mock.core.component.MockHstResponse;
import org.hippoecm.hst.mock.core.container.MockComponentManager;
import org.hippoecm.hst.mock.core.request.MockComponentConfiguration;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.onehippo.cms7.essentials.components.paging.DefaultPagination;
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.springframework.mock.web.MockServletContext;
import uk.nhs.digital.intranet.beans.Task;
import uk.nhs.digital.intranet.enums.SearchArea;
import uk.nhs.digital.intranet.factory.PersonFactory;
import uk.nhs.digital.intranet.model.Person;
import uk.nhs.digital.intranet.model.exception.ProviderCommunicationException;
import uk.nhs.digital.intranet.provider.BloomreachSearchProvider;
import uk.nhs.digital.intranet.provider.GraphProvider;
import uk.nhs.digital.intranet.utils.Constants;

import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class SearchPageComponentTest {

    private static final String REQUEST_ATTR_QUERY = "query";

    private SearchPageComponent underTest;

    @Mock
    private BloomreachSearchProvider bloomreachSearchProvider;

    @Mock
    private PersonFactory personFactory;

    @Mock
    private GraphProvider graphProvider;

    private MockHstRequestContext mockHstRequestContext;

    @Before
    public void setUp() {
        HstServices.setComponentManager(new MockComponentManager());
        mockHstRequestContext = new MockHstRequestContext();
        mockHstRequestContext.setParameterInfoProxyFactory(new HstParameterInfoProxyFactoryImpl());
        ModifiableRequestContextProvider.set(mockHstRequestContext);
        underTest = new SearchPageComponent(personFactory, bloomreachSearchProvider, "applicationId", "redirectUri", "baseUri");
        underTest.init(new MockServletContext(), new MockComponentConfiguration());
    }

    @Test
    public void blankQueryReturnsDocumentsForAllDocTypes() {
        MockHstRequest request = new MockHstRequest();
        request.setParameterMap("", Collections.emptyMap());
        Pageable<HippoBean> documentResults = new DefaultPagination<>(Collections.singletonList(new Task()));
        Mockito.when(bloomreachSearchProvider.getBloomreachResults(Mockito.nullable(String.class), anyInt(), anyInt(),
            SearchArea.INDIVIDUAL_DOCUMENT_SEARCH_AREAS, null, null, null, null)).thenReturn(documentResults);

        underTest.doBeforeRender(request, new MockHstResponse());

        assertEquals(documentResults, request.getAttribute(REQUEST_ATTR_PAGEABLE));
    }

    @Test
    public void peopleResultsEmptyIfNotLoggedIn() throws ProviderCommunicationException {
        MockHstRequest request = new MockHstRequest();
        request.setParameterMap("", Collections.emptyMap());
        request.addParameter(REQUEST_ATTR_QUERY, "queryString is here");
        mockHstRequestContext.setAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME, "");

        underTest.doBeforeRender(request, new MockHstResponse());

        Mockito.verify(graphProvider, Mockito.never()).getUsers(Mockito.nullable(String.class), anyInt());
        assertNull(request.getAttribute(REQUEST_ATTR_PAGEABLE));
    }

    @Test
    public void peopleResultsEmptyIfQueryEmpty() throws ProviderCommunicationException {
        MockHstRequest request = new MockHstRequest();
        request.setParameterMap("", Collections.emptyMap());
        request.addParameter(REQUEST_ATTR_QUERY, "");
        mockHstRequestContext.setAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME, "logged-into-microsoft-access-token");

        underTest.doBeforeRender(request, new MockHstResponse());

        Mockito.verify(graphProvider, Mockito.never()).getUsers(Mockito.nullable(String.class), anyInt());
        assertNull(request.getAttribute(REQUEST_ATTR_PAGEABLE));
    }

    @Test
    public void newsQueryLimitsSearchAreaAndPopulatesTabs() {
        MockHstRequest request = new MockHstRequest();
        request.setParameterMap("", Collections.emptyMap());
        request.addParameter(REQUEST_ATTR_AREA, "news");
        int newsResults = 53;
        int tasksResults = 5;
        int teamsResults = 2;
        Pageable<HippoBean> documentResults = new DefaultPagination<>(Collections.singletonList(new Task()), newsResults);
        Mockito.when(bloomreachSearchProvider.getBloomreachResults(Mockito.nullable(String.class), anyInt(), anyInt(),
            eq(SearchArea.INDIVIDUAL_DOCUMENT_SEARCH_AREAS), null, null, null, null)).thenReturn(documentResults);
        Mockito.when(bloomreachSearchProvider.getBloomreachResultsCount(null, Collections.singletonList(SearchArea.TASKS), null, null, null)).thenReturn(tasksResults);
        Mockito.when(bloomreachSearchProvider.getBloomreachResultsCount(null, Collections.singletonList(SearchArea.TEAMS), null, null, null)).thenReturn(teamsResults);

        underTest.doBeforeRender(request, new MockHstResponse());

        assertEquals(documentResults, request.getAttribute(REQUEST_ATTR_PAGEABLE));
        Mockito.verify(bloomreachSearchProvider, Mockito.never()).getBloomreachResultsCount(Mockito.nullable(String.class),
            eq(Collections.singletonList(SearchArea.NEWS)), null, null, null);
    }

    @Test
    public void peopleQueryReturnsResultsAndPopulatesTabs() throws ProviderCommunicationException {
        MockHstRequest request = new MockHstRequest();
        request.setParameterMap("", Collections.emptyMap());
        request.addParameter(REQUEST_ATTR_QUERY, "query string here");
        mockHstRequestContext.setAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME, "logged-into-microsoft-access-token");
        request.addParameter(REQUEST_ATTR_AREA, "people");
        int newsResults = 13;
        int tasksResults = 5;
        int teamsResults = 2;
        Mockito.when(bloomreachSearchProvider.getBloomreachResultsCount(anyString(), eq(Collections.singletonList(SearchArea.NEWS)), null, null, null)).thenReturn(newsResults);
        Mockito.when(bloomreachSearchProvider.getBloomreachResultsCount(anyString(), eq(Collections.singletonList(SearchArea.TASKS)), null, null, null)).thenReturn(tasksResults);
        Mockito.when(bloomreachSearchProvider.getBloomreachResultsCount(anyString(), eq(Collections.singletonList(SearchArea.TEAMS)), null, null, null)).thenReturn(teamsResults);

        List<Person> expectedPeopleResults = Collections.singletonList(new Person("1", "bob", "finance"));
        Mockito.when(personFactory.createPersons(any())).thenReturn(expectedPeopleResults);

        underTest.doBeforeRender(request, new MockHstResponse());

        assertEquals(expectedPeopleResults, request.getAttribute(REQUEST_ATTR_PAGEABLE));
        Mockito.verify(bloomreachSearchProvider, Mockito.never()).getBloomreachResults(anyString(), anyInt(), anyInt(), any(), any(), any(), any(), null);
    }
}
