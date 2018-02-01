package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.taxonomy.api.Category;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

import java.util.Locale;

public class FacetComponent extends SearchComponent {
    private static Logger log = LoggerFactory.getLogger(FacetComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
        Taxonomy taxonomy = taxonomyManager.getTaxonomies().getTaxonomy(HippoBeanHelper.getTaxonomyName());
        TaxonomyWrapper taxonomyWrapper = new TaxonomyWrapper(taxonomy);

        request.setAttribute("taxonomy", taxonomyWrapper);
        request.setAttribute("query", getQueryParameter(request));
        request.setAttribute("facets", getFacetNavigationBean(request));
        request.setAttribute("cparam", getComponentInfo(request)) ;
    }

    public class TaxonomyWrapper {
        private final Taxonomy taxonomy;

        private TaxonomyWrapper(Taxonomy taxonomy) {
            this.taxonomy = taxonomy;
        }

        public String getValueName(String key) {
            Category taxonomyCategory = taxonomy.getCategoryByKey(key);
            if (taxonomyCategory == null) {
                log.error("No taxonomy for key: " + key);
                return "Invalid Taxonomy: " + key;
            } else {
                return taxonomyCategory.getInfo(Locale.UK).getName();
            }
        }
    }
}
