package uk.nhs.digital.common.components.apicatalogue;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static uk.nhs.digital.test.TestLogger.LogAssertor.error;
import static uk.nhs.digital.test.util.RandomTestUtils.randomString;

import com.google.common.collect.ImmutableSet;
import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstParameterInfoProxyFactoryImpl;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.core.container.HstContainerURL;
import org.hippoecm.hst.core.container.HstContainerURLImpl;
import org.hippoecm.hst.mock.core.component.MockHstRequest;
import org.hippoecm.hst.mock.core.component.MockHstResponse;
import org.hippoecm.hst.mock.core.request.MockHstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.onehippo.repository.mock.MockNode;
import org.onehippo.repository.mock.MockSession;
import org.powermock.core.classloader.annotations.PrepareOnlyThisForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import uk.nhs.digital.cache.Cache;
import uk.nhs.digital.common.components.apicatalogue.filters.Filters;
import uk.nhs.digital.common.components.apicatalogue.filters.FiltersFactory;
import uk.nhs.digital.common.components.apicatalogue.repository.ApiCatalogueRepository;
import uk.nhs.digital.test.TestLoggerRule;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;
import uk.nhs.digital.website.beans.ComponentList;
import uk.nhs.digital.website.beans.Externallink;
import uk.nhs.digital.website.beans.Internallink;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({ApiCatalogueContext.class, ApiCatalogueComponent.class})
public class ApiCatalogueComponentTest extends MockitoSessionTestBase {

    @Rule
    public TestLoggerRule logger = TestLoggerRule.targeting(ApiCatalogueComponent.class);

    private static final String REQUEST_ATTR_RESULTS = "apiCatalogueLinks";
    private static final String REQUEST_ATTR_FILTERS = "filtersModel";

    private ApiCatalogueComponent apiCatalogueComponent;

    @Mock private ComponentManager componentManager;
    @Mock private ApiCatalogueRepository apiCatalogueRepository;
    @Mock private Filters expectedFiltersFromFactory;
    @Mock private FiltersFactory filtersFactory;

    private HstContainerURL hstContainerUrl;
    private MockHstRequest request;
    private MockHstRequestContext hstRequestContext;

    private MockSession session = new MockSession(new MockNode("root"));

    private MockHstResponse irrelevantResponse;
    private String expectedMappingYaml;
    private ComponentList apiCatalogueDocument;
    private List<?> allCatalogueLinksToTaggedDocuments;

    @Before
    public void setUp() throws Exception {

        expectedMappingYaml = "valid: 'mapping YAML'";

        given(apiCatalogueRepository.taxonomyFiltersMapping()).willReturn(Optional.ofNullable(expectedMappingYaml));

        givenApiCatalogueContext();

        givenApiCatalogueDocumentWithInternalLinksToCatalogueDocuments();

        HstServices.setComponentManager(componentManager);

        givenRequestWithApiCatalogueAsContentBean();

        irrelevantResponse = new MockHstResponse();

        apiCatalogueComponent = new ApiCatalogueComponent();
    }

    @Test
    public void listsAllApiCatalogueDocs_excludingRetiredApis_whenUserSelectedFiltersApplied_andShowRetiredNotApplied() {

        // given
        final Set<String> allFilterKeysOfAllDocsTaggedWithAllUserSelectedKeys
            = ImmutableSet.of("fhir", "hl7-v3", "inpatient", "hospital", "mental-health", "dental-health", "deprecated-api");

        final Set<String> noUserSelectedFilterKeys = emptySet();

        given(expectedFiltersFromFactory.initialisedWith(
            allFilterKeysOfAllDocsTaggedWithAllUserSelectedKeys,
            noUserSelectedFilterKeys
        )).willReturn(expectedFiltersFromFactory);

        // when
        apiCatalogueComponent.doBeforeRender(request, irrelevantResponse);

        // then
        final List<?> actualResults = (List<?>) request.getAttribute(REQUEST_ATTR_RESULTS);
        assertThat(
            "Results comprise links of all docs referenced from API catalogue, except of docs tagged as Retired.",
            actualResults,
            is(asList(
                allCatalogueLinksToTaggedDocuments.get(0),
                allCatalogueLinksToTaggedDocuments.get(1),
                allCatalogueLinksToTaggedDocuments.get(2),
                allCatalogueLinksToTaggedDocuments.get(3),
                allCatalogueLinksToTaggedDocuments.get(4),
                allCatalogueLinksToTaggedDocuments.get(6),
                allCatalogueLinksToTaggedDocuments.get(7)
            ))
        );

        final Filters actualFilters = (Filters) request.getAttribute(REQUEST_ATTR_FILTERS);
        assertThat(
            "Filters are as produced by the filters factory.",
            actualFilters,
            sameInstance(expectedFiltersFromFactory)
        );
    }

    @Test
    public void listsAllApiCatalogueDocs_includingRetiredApis_whenUserSelectedFiltersNotApplied_andShowRetiredApplied() {

        // given
        final Set<String> allFilterKeysOfAllDocsTaggedWithAllUserSelectedKeys
            = ImmutableSet.of("fhir", "hl7-v3", "inpatient", "hospital", "mental-health", "dental-health", "deprecated-api", "retired-api");

        final Set<String> noUserSelectedFilterKeys = emptySet();

        given(expectedFiltersFromFactory.initialisedWith(
            allFilterKeysOfAllDocsTaggedWithAllUserSelectedKeys,
            noUserSelectedFilterKeys
        )).willReturn(expectedFiltersFromFactory);

        request.setQueryString("showRetired");

        // when
        apiCatalogueComponent.doBeforeRender(request, irrelevantResponse);

        // then
        final List<?> actualResults = (List<?>) request.getAttribute(REQUEST_ATTR_RESULTS);
        assertThat(
            "Results comprise links of all docs referenced from API catalogue, including of docs tagged as Retired.",
            actualResults,
            is(allCatalogueLinksToTaggedDocuments)
        );

        final Filters actualFilters = (Filters) request.getAttribute(REQUEST_ATTR_FILTERS);
        assertThat(
            "Filters are as produced by the filters factory.",
            actualFilters,
            sameInstance(expectedFiltersFromFactory)
        );
    }

    @Test
    public void listsApiCatalogueDocs_taggedWithAllAppliedFilters_includingRetired_whenUserSelectedFiltersApplied_andShowRetiredApplied() {

        // given
        final Set<String> userSelectedFilterKeys = ImmutableSet.of("fhir", "retired-api");

        // @formatter:off
        final Set<String> allFilterKeysOfAllDocsTaggedWithAllUserSelectedKeys = ImmutableSet.of(
            "fhir",                 // user-selected
            "retired-api"           // user-selected
        );
        // @formatter:on

        given(expectedFiltersFromFactory.initialisedWith(
            allFilterKeysOfAllDocsTaggedWithAllUserSelectedKeys,
            userSelectedFilterKeys
        )).willReturn(expectedFiltersFromFactory);

        hstContainerUrl.setParameter("filter", userSelectedFilterKeys.toArray(new String[0]));

        request.setQueryString("showRetired");

        // when
        apiCatalogueComponent.doBeforeRender(request, irrelevantResponse);

        // then
        final List<?> actualResults = (List<?>) request.getAttribute(REQUEST_ATTR_RESULTS);
        assertThat(
            "Results comprise links of all docs referenced from API catalogue, that are tagged with user selected filter keys, including of docs tagged as Retired.",
            actualResults,
            is(Collections.singletonList(
                allCatalogueLinksToTaggedDocuments.get(5)
            ))
        );

        final Filters filters = (Filters) request.getAttribute(REQUEST_ATTR_FILTERS);
        assertThat(
            "Filters are as produced by the filters factory.",
            filters,
            sameInstance(expectedFiltersFromFactory)
        );
    }

    @Test
    public void listsApiCatalogueDocs_taggedWithAllAppliedFilters_excludingRetired_whenUserSelectedFiltersApplied_andShowRetiredNotApplied() {

        // given
        final Set<String> userSelectedFilterKeys = ImmutableSet.of(
            "fhir",
            "inpatient"
        );

        hstContainerUrl.setParameter("filter", userSelectedFilterKeys.toArray(new String[0]));

        // @formatter:off
        final Set<String> allFilterKeysOfAllDocsTaggedWithAllUserSelectedKeys = ImmutableSet.of(
            "fhir",             // user-selected
            "inpatient",        // user-selected
            "mental-health"
        );
        // @formatter:on

        given(expectedFiltersFromFactory.initialisedWith(
            allFilterKeysOfAllDocsTaggedWithAllUserSelectedKeys,
            userSelectedFilterKeys
        )).willReturn(expectedFiltersFromFactory);

        // when
        apiCatalogueComponent.doBeforeRender(request, irrelevantResponse);

        // then
        final List<?> actualResults = (List<?>) request.getAttribute(REQUEST_ATTR_RESULTS);
        assertThat(
            "Results comprise links of all docs referenced from API catalogue, that are tagged with user selected filter keys, except of docs tagged as Retired.",
            actualResults,
            is(asList(
                allCatalogueLinksToTaggedDocuments.get(1),
                allCatalogueLinksToTaggedDocuments.get(3)
            ))
        );

        final Filters filters = (Filters) request.getAttribute(REQUEST_ATTR_FILTERS);
        assertThat(
            "Filters are as produced by the filters factory.",
            filters,
            sameInstance(expectedFiltersFromFactory)
        );
    }

    @Test
    public void doesNotFail_whenBuildingOfTheFiltersModelFails() {

        // given
        given(filtersFactory.filtersFromMappingYaml(any(String.class))).willThrow(new RuntimeException("Invalid YAML."));

        // when
        apiCatalogueComponent.doBeforeRender(request, irrelevantResponse);

        // then
        final List<?> actualResults = (List<?>) request.getAttribute(REQUEST_ATTR_RESULTS);
        assertThat(
            "Results comprise all links from the API catalogue document.",
            actualResults,
            is(asList(
                allCatalogueLinksToTaggedDocuments.get(0),
                allCatalogueLinksToTaggedDocuments.get(1),
                allCatalogueLinksToTaggedDocuments.get(2),
                allCatalogueLinksToTaggedDocuments.get(3),
                allCatalogueLinksToTaggedDocuments.get(4),
                allCatalogueLinksToTaggedDocuments.get(6),
                allCatalogueLinksToTaggedDocuments.get(7)
            ))
        );

        final Filters actualFilters = (Filters) request.getAttribute(REQUEST_ATTR_FILTERS);
        assertThat(
            "Filters are as produced by the filters factory.",
            actualFilters,
            is(Filters.emptyInstance())
        );

        logger.shouldReceive(
            error("Failed to generate Filters model.")
                .withException("Invalid YAML.")
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void givenApiCatalogueDocumentWithInternalLinksToCatalogueDocuments() {

        // @formatter:off
        allCatalogueLinksToTaggedDocuments = asList(
            internalLinkToDocTaggedWith("hl7-v3", "dental-health"),                 // [0]
            internalLinkToDocTaggedWith("fhir",   "inpatient", "mental-health"),    // [1]
            internalLinkToDocTaggedWith("fhir",   "hospital"),                      // [2]
            internalLinkToDocTaggedWith("fhir",   "inpatient"),                     // [3]
            internalLinkToDocTaggedWith("fhir",   "deprecated-api"),                // [4]
            internalLinkToDocTaggedWith("fhir",   "retired-api"),                   // [5]
            internalLinkToDocTaggedWith("hl7-v3", "deprecated-api"),                // [6]
            externalLink("https://www.google.com")                                  // [7]
        );
        // @formatter:on

        apiCatalogueDocument = mock(ComponentList.class);

        given(apiCatalogueDocument.getBlocks()).willReturn((List)allCatalogueLinksToTaggedDocuments);
    }

    private Internallink internalLinkToDocTaggedWith(final String... taxonomyKeys) {
        final HippoBean bean = mock(HippoBean.class);
        given(bean.getProperties()).willReturn(Collections.singletonMap("hippotaxonomy:keys", taxonomyKeys));

        final Internallink link = mock(Internallink.class);
        given(link.getLinkType()).willReturn("internal");
        given(link.getLink()).willReturn(bean);
        given(link.toString()).willReturn("Internallink of a doc tagged with: " + String.join(", ", taxonomyKeys));

        return link;
    }

    private Externallink externalLink(final String url) {
        final Externallink link = mock(Externallink.class);

        given(link.getLinkType()).willReturn("external");
        given(link.getLink()).willReturn(url);
        given(link.toString()).willReturn("Externallink: " + url);

        return link;
    }

    private void givenApiCatalogueContext() {
        mockStatic(ApiCatalogueContext.class);

        given(ApiCatalogueContext.apiCatalogueRepository(session)).willReturn(apiCatalogueRepository);
        given(ApiCatalogueContext.filtersFactory()).willReturn(filtersFactory);
        given(filtersFactory.filtersFromMappingYaml(expectedMappingYaml)).willReturn(expectedFiltersFromFactory);
    }

    private void givenRequestWithApiCatalogueAsContentBean() {

        hstContainerUrl = new HstContainerURLImpl();

        hstRequestContext = new MockHstRequestContext();
        hstRequestContext.setSession(session);
        hstRequestContext.setParameterInfoProxyFactory(new HstParameterInfoProxyFactoryImpl());
        hstRequestContext.setContentBean(apiCatalogueDocument);
        hstRequestContext.setBaseURL(hstContainerUrl);

        ModifiableRequestContextProvider.set(hstRequestContext);

        request = new MockHstRequest();
        request.setRequestContext(hstRequestContext);
        request.setRequestURL(new StringBuffer("http://localhost:8080/site/developer/api-catalogue"));
    }

}
