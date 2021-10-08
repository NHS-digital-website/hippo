package uk.nhs.digital.intranet.components;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static uk.nhs.digital.intranet.components.SearchPageComponent.PEOPLE_API_LIMIT;
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
import org.onehippo.cms7.essentials.components.paging.Pageable;
import org.springframework.mock.web.MockServletContext;
import uk.nhs.digital.intranet.enums.SearchTypes;
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

    private MockComponentConfiguration componentConfiguration;

    @Mock
    private BloomreachSearchProvider bloomreachSearchProvider;

    @Mock
    private PersonFactory personFactory;

    @Mock
    private GraphProvider graphProvider;

    private MockHstRequestContext mockHstRequestContext;

    @Mock Pageable<HippoBean> results;

    @Before
    public void setUp() {
        HstServices.setComponentManager(new MockComponentManager());
        mockHstRequestContext = new MockHstRequestContext();
        mockHstRequestContext.setParameterInfoProxyFactory(new HstParameterInfoProxyFactoryImpl());
        ModifiableRequestContextProvider.set(mockHstRequestContext);
        underTest = new SearchPageComponent(personFactory, bloomreachSearchProvider, "applicationId", "redirectUri", "baseUri");
        componentConfiguration = new MockComponentConfiguration();
        underTest.init(new MockServletContext(), componentConfiguration);
    }

    @Test
    public void blankQueryReturnsNull() {
        MockHstRequest request = new MockHstRequest();
        request.setParameterMap("", Collections.emptyMap());

        Mockito.when(bloomreachSearchProvider.getBloomreachResults(Mockito.nullable(String.class), anyInt(), anyInt(),
            eq(SearchTypes.INDIVIDUAL_DOCUMENT_SEARCH_TYPES), nullable(List.class), nullable(List.class), nullable(List.class),
            nullable(String.class))).thenReturn(results);

        underTest.doBeforeRender(request, new MockHstResponse());

        assertNull(request.getAttribute(REQUEST_ATTR_PAGEABLE));
    }

    @Test
    public void peopleResultsEmptyIfNotLoggedIn() throws ProviderCommunicationException {
        MockHstRequest request = new MockHstRequest();
        request.setParameterMap("", Collections.emptyMap());
        request.addParameter(REQUEST_ATTR_AREA, "PEOPLE");
        request.addParameter(REQUEST_ATTR_QUERY, "queryString is here");
        componentConfiguration.addParameter(PEOPLE_API_LIMIT, "30");

        mockHstRequestContext.setAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME, "");

        underTest.doBeforeRender(request, new MockHstResponse());

        Mockito.verify(personFactory, Mockito.never()).fetchPeople(anyString(), anyInt());
        assertNull(request.getAttribute(REQUEST_ATTR_PAGEABLE));
    }

    @Test
    public void peopleResultsEmptyIfQueryEmpty() {
        MockHstRequest request = new MockHstRequest();
        request.setParameterMap("", Collections.emptyMap());
        request.addParameter(REQUEST_ATTR_QUERY, "");
        componentConfiguration.addParameter(PEOPLE_API_LIMIT, "30");
        mockHstRequestContext.setAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME, "logged-into-microsoft-access-token");
        request.addParameter(REQUEST_ATTR_AREA, "PEOPLE");

        underTest.doBeforeRender(request, new MockHstResponse());

        Mockito.verify(personFactory, Mockito.never()).fetchPeople(anyString(), anyInt());
        assertNull(request.getAttribute(REQUEST_ATTR_PAGEABLE));
    }

    @Test
    public void peopleQueryReturnsResults() {
        MockHstRequest request = new MockHstRequest();
        request.setParameterMap("", Collections.emptyMap());
        request.addParameter(REQUEST_ATTR_QUERY, "query string here");
        mockHstRequestContext.setAttribute(Constants.ACCESS_TOKEN_PROPERTY_NAME, "logged-into-microsoft-access-token");
        componentConfiguration.addParameter(PEOPLE_API_LIMIT, "30");
        request.addParameter(REQUEST_ATTR_AREA, "PEOPLE");

        List<Person> expectedPeopleResults = Collections.singletonList(new Person("1", "bob", "finance"));
        Mockito.when(personFactory.fetchPeople(anyString(), anyInt())).thenReturn(expectedPeopleResults);
        Mockito.when(personFactory.fetchPhotos(expectedPeopleResults)).thenReturn(expectedPeopleResults);

        underTest.doBeforeRender(request, new MockHstResponse());

        Mockito.verify(bloomreachSearchProvider, Mockito.never()).getBloomreachResults(anyString(), anyInt(), anyInt(),
            eq(SearchTypes.INDIVIDUAL_DOCUMENT_SEARCH_TYPES), any(), any(), any(), any());

        Person personResult = ((PeopleSearchPageable) request.getAttribute(REQUEST_ATTR_PAGEABLE)).getItems().get(0);
        assertEquals(expectedPeopleResults.get(0), personResult);
    }
}
