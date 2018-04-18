package uk.nhs.digital.common.components;

import static java.util.stream.Collectors.toList;

import org.hippoecm.hst.content.beans.standard.HippoFolderBean;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

public class TaxonomyFacet {
    private static final Logger log = LoggerFactory.getLogger(TaxonomyFacet.class);

    private final Category taxonomyCategory;
    private final HippoFolderBean facetBean;

    private List<TaxonomyFacet> children = new ArrayList<>();

    public TaxonomyFacet(Taxonomy taxonomy, String key, HippoFolderBean facetBean) {
        this.taxonomyCategory = taxonomy.getCategoryByKey(key);
        this.facetBean = facetBean;
    }

    public String getValueName() {
        if (taxonomyCategory == null) {
            log.error("No taxonomy for key: " + facetBean.getName());

            return "Invalid Taxonomy: " + facetBean.getName();
        } else {
            return taxonomyCategory.getInfo(Locale.UK).getName();
        }
    }

    public List<TaxonomyFacet> getChildren() {
        return children;
    }

    public void addChild(TaxonomyFacet taxonomyFacet) {
        children.add(taxonomyFacet);
    }

    public Category getTaxonomyCategory() {
        return taxonomyCategory;
    }

    public HippoFolderBean getFacetBean() {
        return facetBean;
    }

    public List<HippoFolderBean> getRemoveList() {
        return Stream.concat(
            Stream.of(this),
            children.stream()
                .flatMap(child ->
                    Stream.concat(
                        Stream.of(child),
                        child.children.stream())))
            .map(TaxonomyFacet::getFacetBean)
            .collect(toList());
    }
}
