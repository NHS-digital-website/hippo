package uk.nhs.digital.common.components;

import static java.util.Collections.emptyList;
import static org.springframework.util.CollectionUtils.isEmpty;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

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

        // get the taxonomy facets
        List<HippoFolderBean> taxonomyFacets = facetBean.getFolders()
            .stream()
            .filter((facet) -> facet.getName().equals(TAXONOMY_FACET_NAME))
            .findAny()
            .map(HippoFolderBean::getFolders)
            .orElseGet(() -> {
                log.error("Unable to find taxonomy facet.");
                return null;
            });

        if (isEmpty(taxonomyFacets)) {
            return emptyList();
        }

        // link the taxonomy facets to the taxonomy category
        HashMap<String, TaxonomyFacet> keyToTaxonomyFacet = new HashMap<>(taxonomyFacets.size());
        for (HippoFolderBean facet : taxonomyFacets) {
            String key = facet.getName();
            keyToTaxonomyFacet.put(key, new TaxonomyFacet(taxonomy, key, facet));
        }

        // build the tree structure
        List<TaxonomyFacet> rootTaxonomyFacets = new ArrayList<>();
        for (TaxonomyFacet taxonomyFacet : keyToTaxonomyFacet.values()) {
            Category taxonomyCategory = taxonomyFacet.getTaxonomyCategory();
            Category parentTaxonomy = taxonomyCategory == null ? null : taxonomyCategory.getParent();

            if (parentTaxonomy == null) {
                rootTaxonomyFacets.add(taxonomyFacet);
            } else {
                String parentKey = parentTaxonomy.getKey();
                TaxonomyFacet parent = keyToTaxonomyFacet.get(parentKey);

                if (parent == null) {
                    log.error("No parent facet found for taxonomy key: " + taxonomyCategory.getName());
                } else {
                    parent.addChild(taxonomyFacet);
                }
            }
        }

        return rootTaxonomyFacets;
    }

}
