package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.*;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.essentials.components.*;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;
import org.onehippo.taxonomy.api.*;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

@ParametersInfo(type = EssentialsFacetsComponentInfo.class)
public class SearchEssentialsFacetsComponent extends EssentialsFacetsComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {

        final String query = SearchQuerySanitizer.getSanitizedSearchQuery(request, this);
        request.setAttribute("query", query);

        if (Boolean.TRUE.equals(request.getAttribute(SearchQuerySanitizer.UNSUPPORTED_QUERY_ATTR))) {
            request.setModel("facets", null);
            request.setAttribute("taxonomy", null);
            return;
        }

        try {
            super.doBeforeRender(request, response);
        } catch (Exception e) {
            request.setModel("facets", null);
            request.setAttribute("taxonomy", null);
            return;
        }

        HippoFacetNavigationBean bean = request.getModel("facets");
        request.setAttribute("taxonomy", buildTaxonomyWrapper(bean));
    }

    private TaxonomyFacetWrapper buildTaxonomyWrapper(HippoFacetNavigationBean bean) {

        if (bean == null) {
            return null;
        }

        ComponentManager manager = HstServices.getComponentManager();
        if (manager == null) {
            return null;
        }

        TaxonomyManager taxonomyManager = manager.getComponent(TaxonomyManager.class.getName());
        if (taxonomyManager == null) {
            return null;
        }

        Taxonomies taxonomies = taxonomyManager.getTaxonomies();
        if (taxonomies == null) {
            return null;
        }

        Taxonomy taxonomy = taxonomies.getTaxonomy(HippoBeanHelper.PUBLICATION_TAXONOMY);
        if (taxonomy == null) {
            return null;
        }

        return new TaxonomyFacetWrapper(taxonomy, bean);
    }
}