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

        Optional<HippoFolderBean> taxonomyFacetRoot = Optional.ofNullable(facetBean.getFolders())
            .orElse(emptyList())
            .stream()
            .filter((facet) -> facet.getName().equals(TAXONOMY_FACET_NAME))
            .findFirst();

        if (!taxonomyFacetRoot.isPresent()) {
            log.warn("Unable to find taxonomy facet: {}", TAXONOMY_FACET_NAME);
            return emptyList();
        }

        List<HippoFolderBean> taxonomyFacets = flattenFacetFolders(taxonomyFacetRoot.get());

        if (isEmpty(taxonomyFacets)) {
            return emptyList();
        }

        // link the taxonomy facets to the taxonomy category
        Map<String, TaxonomyFacet> keyToTaxonomyFacet = new LinkedHashMap<>(taxonomyFacets.size());
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

    private List<HippoFolderBean> flattenFacetFolders(HippoFolderBean taxonomyFacetRoot) {
        List<HippoFolderBean> flattenedFolders = new ArrayList<>();
        Deque<HippoFolderBean> queue = new ArrayDeque<>(getChildFolders(taxonomyFacetRoot));

        while (!queue.isEmpty()) {
            HippoFolderBean current = queue.removeFirst();
            flattenedFolders.add(current);

            List<HippoFolderBean> children = getChildFolders(current);
            if (!children.isEmpty()) {
                queue.addAll(children);
            }
        }

        return flattenedFolders;
    }

    private List<HippoFolderBean> getChildFolders(HippoFolderBean folder) {
        List<HippoFolderBean> childFolders = folder.getFolders();
        return isEmpty(childFolders) ? emptyList() : childFolders;
    }

}
