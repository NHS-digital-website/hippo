package uk.nhs.digital.common.components;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;
import static uk.nhs.digital.common.components.TaxonomyFacetWrapper.TAXONOMY_FACET_NAME;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.CategoryInfo;
import org.onehippo.taxonomy.api.Taxonomy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class TaxonomyFacetWrapperTest {

    @Mock private Taxonomy taxonomy;
    @Mock private HippoFacetNavigationBean facetBean;
    @Mock private HippoFolderBean taxonomyFacet;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        given(facetBean.getFolders())
            .willReturn(Collections.singletonList(taxonomyFacet));

        given(taxonomy.getCategoryByKey(anyString()))
            .willAnswer(invocation -> mockCategory(invocation.getArgument(0)));

        given(taxonomyFacet.getName())
            .willReturn(TAXONOMY_FACET_NAME);

        List<HippoFolderBean> taxonomyFacetBeans = createTaxonomyFacetBeans();
        given(taxonomyFacet.getFolders())
            .willReturn(taxonomyFacetBeans);
    }

    @Test
    public void test() {
        List<TaxonomyFacet> rootTaxonomyFacets = new TaxonomyFacetWrapper(taxonomy, facetBean).getRootTaxonomyFacets();

        assertTaxonomyFacets(rootTaxonomyFacets, "Taxonomy 1", "Taxonomy 2");

        List<TaxonomyFacet> taxonomy1Children = assertTaxonomyChildFacets(rootTaxonomyFacets, "Taxonomy 1", "Taxonomy 1 1", "Taxonomy 1 2");

        List<TaxonomyFacet> taxonomy2Children = assertTaxonomyChildFacets(rootTaxonomyFacets, "Taxonomy 2", "Taxonomy 2 1", "Taxonomy 2 2");

        assertTaxonomyChildFacets(taxonomy1Children, "Taxonomy 1 2", "Taxonomy 1 2 1", "Taxonomy 1 2 2");

        assertTaxonomyChildFacets(taxonomy2Children, "Taxonomy 2 2", "Taxonomy 2 2 1", "Taxonomy 2 2 2");
    }

    private Category mockCategory(String key) {
        Category category = mock(Category.class);

        given(category.getKey())
            .willReturn(key);

        CategoryInfo categoryInfo = mock(CategoryInfo.class);
        given(category.getInfo(Locale.UK))
            .willReturn(categoryInfo);

        given(categoryInfo.getName())
            .willReturn(capitalize(key).replace('_', ' '));

        // If the key is not a root one, set the parent
        if (!key.matches("taxonomy_\\d")) {
            Category parent = mockCategory(key.replaceFirst("_\\d$", ""));
            given(category.getParent())
                .willReturn(parent);
        }

        return category;
    }

    private List<HippoFolderBean> createTaxonomyFacetBeans() {
        return Arrays.asList(
            mockTaxonomyFacetBean("taxonomy_1"),
            mockTaxonomyFacetBean("taxonomy_1_1"),
            mockTaxonomyFacetBean("taxonomy_1_2"),
            mockTaxonomyFacetBean("taxonomy_1_2_1"),
            mockTaxonomyFacetBean("taxonomy_1_2_2"),
            mockTaxonomyFacetBean("taxonomy_2"),
            mockTaxonomyFacetBean("taxonomy_2_1"),
            mockTaxonomyFacetBean("taxonomy_2_2"),
            mockTaxonomyFacetBean("taxonomy_2_2_1"),
            mockTaxonomyFacetBean("taxonomy_2_2_2")
        );
    }

    private HippoFolderBean mockTaxonomyFacetBean(String taxonomyKey) {
        HippoFolderBean bean = mock(HippoFolderBean.class);
        given(bean.getName()).willReturn(taxonomyKey);
        return bean;
    }

    private List<TaxonomyFacet> assertTaxonomyChildFacets(List<TaxonomyFacet> parents, String parent, String... expected) {
        List<TaxonomyFacet> children = getChildren(parents, parent);
        assertTaxonomyFacets(children, expected);
        return children;
    }

    private void assertTaxonomyFacets(List<TaxonomyFacet> taxonomyFacets, String... expected) {
        List<String> rootNames = getTaxonomyNames(taxonomyFacets);
        assertThat("Taxonomy facet tree node is correct", rootNames, containsInAnyOrder(expected));
    }

    private List<TaxonomyFacet> getChildren(List<TaxonomyFacet> parents, String parent) {
        return parents.stream()
            .filter((facet) -> facet.getValueName().equals(parent))
            .findFirst()
            .map(TaxonomyFacet::getChildren)
            .get();
    }

    private List<String> getTaxonomyNames(List<TaxonomyFacet> taxonomyFacets) {
        return taxonomyFacets.stream()
            .map(TaxonomyFacet::getValueName)
            .collect(toList());
    }
}
