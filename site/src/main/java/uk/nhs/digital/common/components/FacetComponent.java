package uk.nhs.digital.common.components;

import org.hippoecm.hst.core.component.HstComponentException;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.site.HstServices;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;
import javax.jcr.RepositoryException;

public class FacetComponent extends SearchComponent {

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
        Taxonomy taxonomy = taxonomyManager.getTaxonomies().getTaxonomy(getTaxonomyName(request.getRequestContext()));

        request.setAttribute("taxonomy", taxonomy);
        request.setAttribute("query", getQueryParameter(request));
        request.setAttribute("facets", getFacetNavigationBean(request));
        request.setAttribute("cparam", getComponentInfo(request)) ;
    }

    // TODO: copied from uk.nhs.digital.ps.components.PublicationComponent. Refactor?
    private String getTaxonomyName(HstRequestContext ctx) throws HstComponentException {
        String taxonomyName;

        try {
            taxonomyName = ctx.getSession().getNode(
                "/hippo:namespaces/publicationsystem/publication/editor:templates/_default_/classifiable")
                .getProperty("essentials-taxonomy-name")
                .getString();
        } catch (RepositoryException e) {
            throw new HstComponentException(
                "Exception occurred during fetching taxonomy file name.", e);
        }

        return taxonomyName;
    }
}
