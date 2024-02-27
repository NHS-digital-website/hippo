package uk.nhs.digital.common.components.catalogue.filters;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.common.components.catalogue.filters.FiltersTestUtils.section;
import static uk.nhs.digital.common.components.catalogue.filters.FiltersTestUtils.subsection;

import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import uk.nhs.digital.common.components.catalogue.CatalogueLink;
import uk.nhs.digital.website.beans.Internallink;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RunWith(DataProviderRunner.class)
public class FiltersAndLinksTests {

    @Test
    public void validResultsForLinksAndFilters() {
        List<String> selectedFilterKeys = ImmutableList.of("inpatient");
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        assertThat("Links are not empty", !filtersAndLinks.links.isEmpty());
        assertThat("Filters are not empty", !filtersAndLinks.filters.isEmpty());
        assertThat("Selected filters are not empty", !filtersAndLinks.selectedFilterKeys.isEmpty());
    }

    @Test
    public void allLinksIncludedWhenNoSelectedFilters() {
        List<String> selectedFilterKeys = Collections.emptyList();
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        assertThat("Links are not empty", !filtersAndLinks.links.isEmpty());
        assertThat("Filters are not empty", !filtersAndLinks.filters.isEmpty());
        assertThat("Selected filters are empty", filtersAndLinks.selectedFilterKeys.isEmpty());
    }

    @Test
    public void linksAreIncludedThatContainEitherOfProvidedFiltersInSameSection() {
        List<String> selectedFilterKeys = ImmutableList.of("inpatient", "outpatient");
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        boolean result = filtersAndLinks.links.stream().anyMatch(link -> !link.allTaxonomyKeysOfReferencedDoc().contains("inpatient"));

        assertThat("Links contain selected filtered documents that are not inpatient", result);
    }

    @Test
    public void linksAreIncludedThatContainBothFirstSectionFilterAndSecondSectionFilter() {
        List<String> selectedFilterKeys = ImmutableList.of("outpatient", "patient");
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        boolean result = filtersAndLinks.links.stream()
            .allMatch(link ->
                link.allTaxonomyKeysOfReferencedDoc().contains("outpatient")
                    && link.allTaxonomyKeysOfReferencedDoc().contains("patient")
            );

        assertThat("Links contain only selected filters from both sections", result);
    }

    @Test
    public void emptySelectedFiltersResultsInAllLinksSet() {
        List<String> selectedFilterKeys = emptyList();
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        boolean result = filtersAndLinks.links.size() == 7;

        assertThat("All links are returned with empty selected filters", result);
    }

    @Test
    public void selectedFilterWithinSectionDoesNotOmitOthersInSameSection() {
        List<String> selectedFilterKeys = ImmutableList.of("outpatient");
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        boolean result = filtersAndLinks.filters.stream().map(navFilter -> navFilter.filterKey).anyMatch(key -> Objects.equals(key, "inpatient"));

        assertThat("Filters contains key in same section as selected key", result);
    }

    @Test
    public void filterInOtherSectionFiltersAllOtherSectionsWithAndLogic() {
        List<String> selectedFilterKeys = ImmutableList.of("outpatient", "patient");
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        boolean result = !filtersAndLinks.filters.contains("inpatient");

        assertThat("inpatient filter from section 1 is not valid after patient is selected so is not in filters", result);
    }

    @Test
    public void filterOrderIsPreserved() {
        List<String> selectedFilterKeys = ImmutableList.of("outpatient", "patient");
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        boolean result = filtersAndLinks.selectedFilterKeys.equals(selectedFilterKeys);

        assertThat("Filter order is preserved", result);
    }

    @Test
    public void invalidUserSelectedFilterIsRemovedAfterInvalidatingSelection() {
        List<String> selectedFilterKeys = ImmutableList.of("inpatient", "outpatient", "patient");
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        boolean result = !filtersAndLinks.selectedFilterKeys.contains("inpatient");

        assertThat("Selected filters does not contain invalid filter after AND", result);
    }

    @Test
    public void filterCountsAreCorrectWithNoSelectedFilters() {
        List<String> selectedFilterKeys = Collections.emptyList();
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        assertThat("Filters are not empty", !filtersAndLinks.filters.isEmpty());
        assertThat("Inpatient filter link count is 3", getCountForKey(filtersAndLinks.filters, "inpatient") == 3);
        assertThat("Outpatient filter link count is 2", getCountForKey(filtersAndLinks.filters, "outpatient") == 2);
        assertThat("Service filter link count is 1", getCountForKey(filtersAndLinks.filters, "service") == 1);
        assertThat("Fhir filter link count is 1", getCountForKey(filtersAndLinks.filters, "fhir") == 1);
        assertThat("Hospital filter link count is 2", getCountForKey(filtersAndLinks.filters, "hospital") == 2);
        assertThat("Patient filter link count is 1", getCountForKey(filtersAndLinks.filters, "patient") == 1);
    }

    @Test
    public void filterCountForSameCategoryAsSelectedFilterIsRetained() {
        List<String> selectedFilterKeys = ImmutableList.of("outpatient");
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        int result = getCountForKey(filtersAndLinks.filters, "inpatient");

        assertThat("Inpatient filter link count is still 3 despite filtering by Outpatient key", result == 3);
    }

    @Test
    public void filterCountForOtherCategoryAsSelectedFilterIsFiltered() {
        List<String> selectedFilterKeys = ImmutableList.of("inpatient");
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters(), selectedFilterKeys);

        int result = getCountForKey(filtersAndLinks.filters, "hospital");

        assertThat("Hospital count is now 1 due to filtering by Inpatient key", result == 1);
    }

    private int getCountForKey(Set<NavFilter> navFilters, String key) {
        return navFilters
                .stream().filter(navFilter -> Objects.equals(navFilter.filterKey, key))
                .findFirst()
                .get().count;
    }


    private Filters baseFilters() {
        return FiltersTestUtils.filters(
            section("Section 1",
                subsection("Subsection 1A", "inpatient"),
                subsection("Subsection 2B", "outpatient")
            ),
            section("Section 2",
                subsection("Subsection 2A", "service"),
                subsection("Subsection 2B", "fhir")
            ),
            section("Section 3",
                subsection("Subsection 3A", "hospital"),
                subsection("Subsection 3B", "patient")
            )
        );
    }

    private FiltersAndLinks filtersAndLinks(Filters filters, List<String> selectedFilterKeys) {
        return new FiltersAndLinks(selectedFilterKeys, catalogueLinks(), filters, null);
    }

    private List<CatalogueLink> catalogueLinks() {
        return asList(
            CatalogueLink.from(internalLinkToDocTaggedWith("inpatient")),
            CatalogueLink.from(internalLinkToDocTaggedWith("outpatient")),
            CatalogueLink.from(internalLinkToDocTaggedWith("inpatient")),
            CatalogueLink.from(internalLinkToDocTaggedWith("hospital", "inpatient")),
            CatalogueLink.from(internalLinkToDocTaggedWith("patient", "outpatient")),
            CatalogueLink.from(internalLinkToDocTaggedWith("service")),
            CatalogueLink.from(internalLinkToDocTaggedWith("fhir", "hospital"))
        );
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
}
