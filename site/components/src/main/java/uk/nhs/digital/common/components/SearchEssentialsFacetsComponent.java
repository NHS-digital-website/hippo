package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.container.ComponentManager;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.cms7.essentials.components.EssentialsFacetsComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;
import org.onehippo.taxonomy.api.Taxonomies;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

@ParametersInfo(
    type = EssentialsFacetsComponentInfo.class
)
public class SearchEssentialsFacetsComponent extends EssentialsFacetsComponent {

    private static final Logger log = LoggerFactory.getLogger(SearchEssentialsFacetsComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);

        HippoFacetNavigationBean hippoFacetNavigationBean = request.getModel("facets");
        request.setAttribute("taxonomy", buildTaxonomyWrapper(hippoFacetNavigationBean));
    }

    private TaxonomyFacetWrapper buildTaxonomyWrapper(HippoFacetNavigationBean hippoFacetNavigationBean) {
        if (hippoFacetNavigationBean == null) {
            return null;
        }

        ComponentManager componentManager = HstServices.getComponentManager();
        if (componentManager == null) {
            log.warn("Cannot look up taxonomy because no ComponentManager is available.");
            return null;
        }

        TaxonomyManager taxonomyManager = componentManager.getComponent(TaxonomyManager.class.getName());
        if (taxonomyManager == null) {
            log.warn("Cannot look up taxonomy because TaxonomyManager is missing from the component manager.");
            return null;
        }

        Taxonomies taxonomies = taxonomyManager.getTaxonomies();
        if (taxonomies == null) {
            log.warn("Cannot look up taxonomy because Taxonomies service returned null.");
            return null;
        }

        Taxonomy taxonomy = taxonomies.getTaxonomy(HippoBeanHelper.PUBLICATION_TAXONOMY);
        if (taxonomy == null) {
            log.warn("Publication taxonomy '{}' was not found.", HippoBeanHelper.PUBLICATION_TAXONOMY);
            return null;
        }

        return new TaxonomyFacetWrapper(taxonomy, hippoFacetNavigationBean);
    }
}
