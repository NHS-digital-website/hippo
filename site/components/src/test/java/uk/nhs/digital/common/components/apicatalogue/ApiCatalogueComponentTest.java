package uk.nhs.digital.common.components.apicatalogue;

import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static uk.nhs.digital.test.TestLogger.LogAssertor.error;

import com.google.common.collect.ImmutableSet;
import org.hippoecm.hst.container.ModifiableRequestContextProvider;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.core.component.HstParameterInfoProxyFactoryImpl;
import org.hippoecm.hst.core.container.ComponentManager;
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
import uk.nhs.digital.common.components.apicatalogue.filters.Filters;
import uk.nhs.digital.common.components.apicatalogue.filters.FiltersFactory;
import uk.nhs.digital.common.components.apicatalogue.repository.ApiCatalogueRepository;
import uk.nhs.digital.test.TestLoggerRule;
import uk.nhs.digital.website.beans.ComponentList;
import uk.nhs.digital.website.beans.Internallink;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest(ApiCatalogueContext.class)
public class ApiCatalogueComponentTest {

    @Rule
    public TestLoggerRule logger = TestLoggerRule.targeting(ApiCatalogueComponent.class);

    private static final String REQUEST_ATTR_RESULTS = "apiCatalogueLinks";
    private static final String REQUEST_ATTR_FILTERS = "filtersModel";

    private ApiCatalogueComponent apiCatalogueComponent;

    @Mock private ComponentManager componentManager;
    @Mock private ApiCatalogueRepository apiCatalogueRepository;
    @Mock private Filters expectedFiltersFromFactory;
    @Mock private FiltersFactory filtersFactory;

    private MockHstRequest request;
    private MockHstRequestContext hstRequestContext;

    private MockSession session = new MockSession(new MockNode("root"));

    private MockHstResponse irrelevantResponse;
    private String expectedMappingYaml;
    private ComponentList apiCatalogueDocument;
    private List<Internallink> allCatalogueLinksToTaggedDocuments;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        expectedMappingYaml = "valid: 'mapping YAML'";

        given(apiCatalogueRepository.taxonomyFiltersMapping()).willReturn(Optional.ofNullable(expectedMappingYaml));

        givenApiCatalogueContext();

        givenApiCatalogueDocumentWithInternalLinksToCalogueDocuments();

        HstServices.setComponentManager(componentManager);

        givenRequestWithApiCatalogueAsContentBean();

        irrelevantResponse = new MockHstResponse();

        apiCatalogueComponent = new ApiCatalogueComponent();
    }

    @Test
    public void listsAllDocumentsFromApiCatalogue_whenNoTagsSelected() {

        // given
        final Set<String> filteredTags_allTagsAppliedAcrossAllCatalogueDocs
            = ImmutableSet.of("fhir", "hl7-v3", "inpatient", "hospital", "mental-health", "dental-health");

        final Set<String> noTagsSelectedByTheUser = emptySet();

        given(expectedFiltersFromFactory.initialisedWith(
            filteredTags_allTagsAppliedAcrossAllCatalogueDocs,
            noTagsSelectedByTheUser
        )).willReturn(expectedFiltersFromFactory);

        // when
        apiCatalogueComponent.doBeforeRender(request, irrelevantResponse);

        // then
        final List<?> actualResults = (List<?>) request.getAttribute(REQUEST_ATTR_RESULTS);
        assertThat(
            "Results comprise all links from the API catalogue document.",
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
    public void listsDocumentsOfApiCatalogue_whereEachDocIsTaggedWithAllSelectedTags_whenAnyTagsSelected() {

        // given
        final Set<String> tagsSelectedByTheUser = ImmutableSet.of("fhir", "inpatient");

        request.addParameter("filters", String.join(",", tagsSelectedByTheUser));

        // @formatter:off
        final Set<String> filteredTags_allTagsAppliedToDocsWithTagsSelectedByTheUser = ImmutableSet.of(
            "fhir",             // selected by the user
            "inpatient",        // selected by the user
            "mental-health"
        );
        // @formatter:on

        given(expectedFiltersFromFactory.initialisedWith(
            filteredTags_allTagsAppliedToDocsWithTagsSelectedByTheUser,
            tagsSelectedByTheUser
        )).willReturn(expectedFiltersFromFactory);

        // when
        apiCatalogueComponent.doBeforeRender(request, irrelevantResponse);

        // then
        final List<?> actualResults = (List<?>) request.getAttribute(REQUEST_ATTR_RESULTS);
        assertThat(
            "Results comprise links to API catalogue docs, each tagged with ALL user-selected taxonomy terms.",
            actualResults,
            is(asList(
                allCatalogueLinksToTaggedDocuments.get(1), // tagged with: fhir, inpatient, mental-health
                allCatalogueLinksToTaggedDocuments.get(3)  // tagged with: fhir, inpatient
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
        given(filtersFactory.filtersFromYaml(any(String.class))).willThrow(new RuntimeException("Invalid YAML."));

        // when
        apiCatalogueComponent.doBeforeRender(request, irrelevantResponse);

        // then
        final List<?> actualResults = (List<?>) request.getAttribute(REQUEST_ATTR_RESULTS);
        assertThat(
            "Results comprise all links from the API catalogue document.",
            actualResults,
            is(allCatalogueLinksToTaggedDocuments)
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
    private void givenApiCatalogueDocumentWithInternalLinksToCalogueDocuments() {

        allCatalogueLinksToTaggedDocuments = asList(
            linkToDocTaggedWith("hl7-v3", "dental-health"),
            linkToDocTaggedWith("fhir",   "inpatient", "mental-health"),
            linkToDocTaggedWith("fhir",   "hospital"),
            linkToDocTaggedWith("fhir",   "inpatient"),
            linkToDocTaggedWith()
        );

        apiCatalogueDocument = mock(ComponentList.class);

        given(apiCatalogueDocument.getBlocks()).willReturn((List)allCatalogueLinksToTaggedDocuments);
    }

    private Internallink linkToDocTaggedWith(final String... taxonomyKeys) {
        HippoBean bean = mock(HippoBean.class);
        given(bean.getProperties()).willReturn(Collections.singletonMap("hippotaxonomy:keys", taxonomyKeys));

        Internallink link = mock(Internallink.class);
        given(link.getLinkType()).willReturn("internal");
        given(link.getLink()).willReturn(bean);
        given(link.toString()).willReturn("Internallink of a doc tagged with: " + String.join(", ", taxonomyKeys));

        return link;
    }

    private void givenApiCatalogueContext() {
        mockStatic(ApiCatalogueContext.class);
        given(ApiCatalogueContext.repository(session)).willReturn(apiCatalogueRepository);
        given(ApiCatalogueContext.filtersFactory()).willReturn(filtersFactory);
        given(filtersFactory.filtersFromYaml(expectedMappingYaml)).willReturn(expectedFiltersFromFactory);
    }

    private void givenRequestWithApiCatalogueAsContentBean() {

        hstRequestContext = new MockHstRequestContext();
        hstRequestContext.setSession(session);
        hstRequestContext.setParameterInfoProxyFactory(new HstParameterInfoProxyFactoryImpl());
        hstRequestContext.setContentBean(apiCatalogueDocument);

        ModifiableRequestContextProvider.set(hstRequestContext);

        request = new MockHstRequest();
        request.setRequestContext(hstRequestContext);
    }

}
