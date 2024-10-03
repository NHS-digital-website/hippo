package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.essentials.components.EssentialsFacetsComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

@ParametersInfo(
    type = EssentialsFacetsComponentInfo.class
)
public class SearchEssentialsFacetsComponent extends EssentialsFacetsComponent {

    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        HippoFacetNavigationBean hippoFacetNavigationBean = request.getModel("facets");
        TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
        Taxonomy taxonomy = taxonomyManager.getTaxonomies().getTaxonomy(HippoBeanHelper.PUBLICATION_TAXONOMY);
        TaxonomyFacetWrapper taxonomyWrapper = hippoFacetNavigationBean == null ? null : new TaxonomyFacetWrapper(taxonomy, hippoFacetNavigationBean);
        request.setAttribute("taxonomy", taxonomyWrapper);
    }
}