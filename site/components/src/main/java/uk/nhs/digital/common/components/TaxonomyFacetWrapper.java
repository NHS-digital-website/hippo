package uk.nhs.digital.common.components;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaxonomyFacetWrapper {

    private static final Logger log = LoggerFactory.getLogger(TaxonomyFacetWrapper.class);

    static final String TAXONOMY_FACET_NAME = "category";

    private final HippoFacetNavigationBean facetBean;
    private final Taxonomy taxonomy;
    private final List<TaxonomyFacet> rootTaxonomyFacets;

    public TaxonomyFacetWrapper(Taxonomy taxonomy, HippoFacetNavigationBean facetBean) {
        this.facetBean = facetBean;
        this.taxonomy = taxonomy;

        this.rootTaxonomyFacets = createTaxonomyFacets();
    }

    public List<TaxonomyFacet> getRootTaxonomyFacets() {
        return rootTaxonomyFacets;
    }

    private List<TaxonomyFacet> createTaxonomyFacets() {

        Optional<HippoFolderBean> taxonomyFacetRoot = Optional.ofNullable(facetBean.getFolders())
            .orElse(emptyList())
            .stream()
            .filter((facet) -> facet.getName().equals(TAXONOMY_FACET_NAME))
            .findFirst();

        if (!taxonomyFacetRoot.isPresent()) {
            log.warn("Unable to find taxonomy facet: {}", TAXONOMY_FACET_NAME);
            return emptyList();
        }

        List<HippoFolderBean> taxonomyFacets = getChildFolders(taxonomyFacetRoot.get());

        if (taxonomyFacets.isEmpty()) {
            return emptyList();
        }

        List<TaxonomyFacet> rootTaxonomyFacets = new ArrayList<>();
        for (HippoFolderBean facet : taxonomyFacets) {
            TaxonomyFacet taxonomyFacet = createTaxonomyFacet(facet, null);
            if (taxonomyFacet != null) {
                rootTaxonomyFacets.add(taxonomyFacet);
            }
        }

        return rootTaxonomyFacets;
    }

    private TaxonomyFacet createTaxonomyFacet(HippoFolderBean facet, String expectedParentKey) {
        TaxonomyFacet taxonomyFacet = new TaxonomyFacet(taxonomy, facet.getName(), facet);
        Category taxonomyCategory = taxonomyFacet.getTaxonomyCategory();

        if (taxonomyCategory != null && !isExpectedParent(taxonomyCategory, expectedParentKey)) {
            log.debug("No parent facet found for taxonomy key: {}", taxonomyCategory.getName());
            return null;
        }

        for (HippoFolderBean childFacet : getChildFolders(facet)) {
            TaxonomyFacet childTaxonomyFacet = createTaxonomyFacet(childFacet, facet.getName());
            if (childTaxonomyFacet != null) {
                taxonomyFacet.addChild(childTaxonomyFacet);
            }
        }

        return taxonomyFacet;
    }

    private boolean isExpectedParent(Category taxonomyCategory, String expectedParentKey) {
        Category parentTaxonomy = taxonomyCategory.getParent();

        if (expectedParentKey == null) {
            return parentTaxonomy == null;
        }

        return parentTaxonomy != null && expectedParentKey.equals(parentTaxonomy.getKey());
    }

    private List<HippoFolderBean> getChildFolders(HippoFolderBean folder) {
        List<HippoFolderBean> childFolders = folder.getFolders();
        return isEmpty(childFolders) ? emptyList() : childFolders;
    }

}
