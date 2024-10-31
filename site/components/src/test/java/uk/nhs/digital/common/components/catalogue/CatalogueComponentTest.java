package uk.nhs.digital.common.components.catalogue;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static uk.nhs.digital.test.TestLogger.LogAssertor.error;

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
import uk.nhs.digital.common.components.catalogue.filters.Filters;
import uk.nhs.digital.common.components.catalogue.filters.FiltersFactory;
import uk.nhs.digital.common.components.catalogue.filters.NavFilter;
import uk.nhs.digital.common.components.catalogue.repository.CatalogueRepository;
import uk.nhs.digital.test.TestLoggerRule;
import uk.nhs.digital.test.mockito.MockitoSessionTestBase;
import uk.nhs.digital.website.beans.ComponentList;
import uk.nhs.digital.website.beans.Externallink;
import uk.nhs.digital.website.beans.Internallink;

import java.util.*;
import java.util.stream.Collectors;

@RunWith(PowerMockRunner.class)
@PrepareOnlyThisForTest({CatalogueContext.class, CatalogueComponent.class})
public class CatalogueComponentTest extends MockitoSessionTestBase {

    @Rule
    public TestLoggerRule logger = TestLoggerRule.targeting(CatalogueComponent.class);

    private static final String REQUEST_ATTR_RESULTS = "catalogueLinks";
    private static final String REQUEST_ATTR_FILTERS = "filtersModel";

    private CatalogueComponent catalogueComponent;

    @Mock private ComponentManager componentManager;
    @Mock private CatalogueRepository catalogueRepository;
    @Mock private Filters expectedFiltersFromFactory;
    @Mock private FiltersFactory filtersFactory;
    @Mock private FacetNavHelper facetNavHelper;

    private HstContainerURL hstContainerUrl;
    private MockHstRequest request;
    private MockHstRequestContext hstRequestContext;

    private MockSession session = new MockSession(new MockNode("root"));

    private MockHstResponse irrelevantResponse;
    private String expectedMappingYaml;
    private ComponentList serviceCatalogueDocument;
    private List<?> allCatalogueLinksToTaggedDocuments;
    private final String taxonomyPath = "/content/documents/administration/website/service-catalogue/taxonomy-filters-mapping";

    @Before
    public void setUp() throws Exception {

        expectedMappingYaml = "valid: 'mapping YAML'";

        given(catalogueRepository.taxonomyFiltersMapping()).willReturn(Optional.ofNullable(expectedMappingYaml));

        givenApiCatalogueContext();

        givenApiCatalogueDocumentWithInternalLinksToCatalogueDocuments();

        HstServices.setComponentManager(componentManager);

        givenRequestWithApiCatalogueAsContentBean();

        irrelevantResponse = new MockHstResponse();

        catalogueComponent = new CatalogueComponent();

        request.getRequestContext().setModel("facetsNavHelper", facetNavHelper);
        when(facetNavHelper.getAllTags()).thenReturn(allTags());
        when(facetNavHelper.getAllTagsForLink(any())).thenReturn(new ArrayList<String>(allTags()));
    }

    @Test
    public void listsAllServiceCatalogueDocs_whenUserSelectedFiltersApplied() {

        // when
        when(expectedFiltersFromFactory.initialisedWith(any(), any())).thenReturn(expectedFiltersFromFactory);
        catalogueComponent.doBeforeRender(request, irrelevantResponse);

        // then
        final List<?> actualResults = (List<?>) request.getAttribute(REQUEST_ATTR_RESULTS);
        assertThat(
            "Results comprise links of all docs referenced from Service catalogue",
            actualResults,
            is(asList(
                allCatalogueLinksToTaggedDocuments.get(0),
                allCatalogueLinksToTaggedDocuments.get(1),
                allCatalogueLinksToTaggedDocuments.get(2),
                allCatalogueLinksToTaggedDocuments.get(3),
                allCatalogueLinksToTaggedDocuments.get(4),
                allCatalogueLinksToTaggedDocuments.get(5),
                allCatalogueLinksToTaggedDocuments.get(6)
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
    public void listsAllServiceCatalogueDocs_whenUserSelectedFiltersNotApplied() {

        // when
        when(expectedFiltersFromFactory.initialisedWith(any(), any())).thenReturn(expectedFiltersFromFactory);
        catalogueComponent.doBeforeRender(request, irrelevantResponse);

        // then
        final List<?> actualResults = (List<?>) request.getAttribute(REQUEST_ATTR_RESULTS);
        assertThat(
            "Results comprise links of all docs referenced from Service catalogue.",
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
    public void doesNotFail_whenBuildingOfTheFiltersModelFails() {

        // given
        given(filtersFactory.filtersFromMappingYaml(any(String.class))).willThrow(new RuntimeException("Invalid YAML."));

        // when
        catalogueComponent.doBeforeRender(request, irrelevantResponse);

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
                        allCatalogueLinksToTaggedDocuments.get(5),
                        allCatalogueLinksToTaggedDocuments.get(6)
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

    private Set<String> allTags() {
        return new HashSet<String>(Arrays.asList("hl7-v3", "dental-health", "fhir", "inpatient", "mental-health", "hospital", "deprecated-api", "retired-api"));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void givenApiCatalogueDocumentWithInternalLinksToCatalogueDocuments() {

        // @formatter:off
        allCatalogueLinksToTaggedDocuments = asList(
            internalLinkToDocTaggedWith("citizen-health", "gp-data"),                           // [0]
            internalLinkToDocTaggedWith("genomics",   "patient-data-layer", "radiology"),       // [1]
            internalLinkToDocTaggedWith("citizen-health",   "appointments-booking-referrals"),  // [2]
            internalLinkToDocTaggedWith("citizen-health",   "registers"),                       // [3]
            internalLinkToDocTaggedWith("radiology",   "registers"),                            // [4]
            internalLinkToDocTaggedWith("interoperability", "gp-data"),                         // [5]
            externalLink("https://www.google.com")                                                       // [6]
        );
        // @formatter:on

        serviceCatalogueDocument = mock(ComponentList.class);

        given(serviceCatalogueDocument.getBlocks()).willReturn((List)allCatalogueLinksToTaggedDocuments);
    }

    private Internallink internalLinkToDocTaggedWith(final String... taxonomyKeys) {
        final HippoBean bean = mock(HippoBean.class);
        given(bean.getProperties()).willReturn(Collections.singletonMap("hippotaxonomy:keys", taxonomyKeys));

        final Internallink link = mock(Internallink.class);
        given(link.getLinkType()).willReturn("internal");
        given(link.getLink()).willReturn(bean);
        given(link.toString()).willReturn("Internallink of a doc tagged with: " + String.join(", ", taxonomyKeys));
        given(link.getIsPublished()).willReturn(true);

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
        mockStatic(CatalogueContext.class);

        given(CatalogueContext.catalogueRepository(session, taxonomyPath)).willReturn(catalogueRepository);
        given(CatalogueContext.filtersFactory()).willReturn(filtersFactory);
        given(filtersFactory.filtersFromMappingYaml(expectedMappingYaml)).willReturn(expectedFiltersFromFactory);
    }

    private void givenRequestWithApiCatalogueAsContentBean() {

        hstContainerUrl = new HstContainerURLImpl();

        hstRequestContext = new MockHstRequestContext();
        hstRequestContext.setSession(session);
        hstRequestContext.setParameterInfoProxyFactory(new HstParameterInfoProxyFactoryImpl());
        hstRequestContext.setContentBean(serviceCatalogueDocument);
        hstRequestContext.setBaseURL(hstContainerUrl);

        ModifiableRequestContextProvider.set(hstRequestContext);

        request = new MockHstRequest();
        request.setRequestContext(hstRequestContext);
        request.setRequestURL(new StringBuffer("http://localhost:8080/site/services/service-catalogue"));
    }

}
