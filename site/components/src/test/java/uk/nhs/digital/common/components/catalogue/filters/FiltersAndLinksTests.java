package uk.nhs.digital.common.components.catalogue.filters;

import com.google.common.collect.ImmutableList;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import uk.nhs.digital.common.components.catalogue.CatalogueLink;
import uk.nhs.digital.website.beans.Internallink;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static uk.nhs.digital.common.components.catalogue.filters.FiltersTestUtils.section;
import static uk.nhs.digital.common.components.catalogue.filters.FiltersTestUtils.subsection;

@RunWith(DataProviderRunner.class)
public class FiltersAndLinksTests {

    @Test
    public void validResultsForLinksAndFilters() {
        FiltersAndLinks filtersAndLinks = filtersAndLinks(baseFilters());

        assertThat("Links are not empty", !filtersAndLinks.links.isEmpty());
        assertThat("Filters are not empty", !filtersAndLinks.filters.isEmpty());
        assertThat("Selected filters are not empty", !filtersAndLinks.validUserSelectedFilterKeys.isEmpty());
    }

    private Filters baseFilters() {
        return FiltersTestUtils.filters(
            section("Section 1",
                subsection("Subsection 1A", "inpatient"),
                subsection("Subsection 2B", "inpatient", "outpatient")
            ),
            section("Section 2",
                subsection("Subsection 2A", "inpatient"),
                subsection("Subsection 2B", "outpatient")
            )
        );
    }

    private FiltersAndLinks filtersAndLinks(Filters filters) {
        List<String> selectedFilterKeys = ImmutableList.of("inpatient");
        return new FiltersAndLinks(selectedFilterKeys, catalogueLinks(), filters);
    }

    private List<CatalogueLink> catalogueLinks() {
        return asList(
            CatalogueLink.from(internalLinkToDocTaggedWith("inpatient")),
            CatalogueLink.from(internalLinkToDocTaggedWith("outpatient")),
            CatalogueLink.from(internalLinkToDocTaggedWith("inpatient"))
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
