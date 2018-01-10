package uk.nhs.digital.ps.migrator.model.taxonomy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import uk.nhs.digital.ps.migrator.model.HippoNode;
import uk.nhs.digital.ps.migrator.model.Property;

public class TaxonomyTerm extends HippoNode {

    private static final String TAXONOMY_KEY = "hippotaxonomy:key";
    private static final String TAXONOMY_NAME = "hippotaxonomy:name";
    private static final String TAXONOMY_CATEGORY = "hippotaxonomy:category";
    private static final String TAXONOMY_CATEGORY_INFO = "hippotaxonomy:categoryinfo";
    private static final String TAXONOMY_CATEGORY_INFOS = "hippotaxonomy:categoryinfos";

    @JsonIgnore
    private final TaxonomyTerm parent;

    public TaxonomyTerm(String name, TaxonomyTerm parent, String primaryType, Property... properties) {
        super(name, primaryType, properties);
        this.parent = parent;
    }

    public TaxonomyTerm addChild(String term) {
        String nodeName = covertTermToKey(term);

        // The structure of these Taxonomy nodes is:
        // Category <- Category Infos <- Category Info <- Term
        TaxonomyTerm childTerm = new TaxonomyTerm(nodeName, this, TAXONOMY_CATEGORY, new Property(TAXONOMY_KEY, false, nodeName));
        addNode(childTerm);

        HippoNode categoryInfos = new HippoNode(TAXONOMY_CATEGORY_INFOS, TAXONOMY_CATEGORY_INFOS);
        childTerm.addNode(categoryInfos);

        HippoNode categoryInfo = new HippoNode("en", TAXONOMY_CATEGORY_INFO, new Property(TAXONOMY_NAME, false, term));
        categoryInfos.addNode(categoryInfo);

        return childTerm;
    }

    public static String covertTermToKey(String term) {
        // Need unique names for the nodes and the taxonomy keys so we just use the
        // term after formatting into a valid path name.
        String key = term.trim()
            .replaceAll("[^A-z]", "-")
            .toLowerCase();

        return key;
    }

    public TaxonomyTerm getParent() {
        return parent;
    }
}
