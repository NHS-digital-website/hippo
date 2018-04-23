package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

public class FacetComponent extends SearchComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
        Taxonomy taxonomy = taxonomyManager.getTaxonomies().getTaxonomy(HippoBeanHelper.getTaxonomyName());
        HippoFacetNavigationBean facetNavigationBean = getFacetNavigationBean(request);
        TaxonomyFacetWrapper taxonomyWrapper = facetNavigationBean == null ? null : new TaxonomyFacetWrapper(taxonomy, facetNavigationBean);

        request.setAttribute("taxonomy", taxonomyWrapper);
        request.setAttribute("query", getQueryParameter(request));
        request.setAttribute("facets", facetNavigationBean);
        request.setAttribute("cparam", getComponentInfo(request)) ;
    }
}
