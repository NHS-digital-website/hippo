package uk.nhs.digital.common.components;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.site.HstServices;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.PathUtils;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;
import org.onehippo.taxonomy.api.Taxonomy;
import org.onehippo.taxonomy.api.TaxonomyManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.nhs.digital.ps.beans.HippoBeanHelper;


@ParametersInfo(
    type = EssentialsFacetsComponentInfo.class
)
public class SearchEssentialsFacetsComponent extends CommonComponent {
    private static Logger log = LoggerFactory.getLogger(org.onehippo.cms7.essentials.components.EssentialsFacetsComponent.class);

    public SearchEssentialsFacetsComponent() {
    }

    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        EssentialsFacetsComponentInfo paramInfo = (EssentialsFacetsComponentInfo) this.getComponentParametersInfo(request);
        HstRequestContext context = request.getRequestContext();
        String facetPath = paramInfo.getFacetPath();
        if (Strings.isNullOrEmpty(facetPath)) {
            HippoBean bean = context.getContentBean();
            if (bean != null) {
                facetPath = bean.getPath();
            }
        }

        String queryParam = this.cleanupSearchQuery(this.getAnyParameter(request, "query"));
        HippoFacetNavigationBean hippoFacetNavigationBean = this.getFacetNavigationBean(context, facetPath, queryParam);

        TaxonomyManager taxonomyManager = HstServices.getComponentManager().getComponent(TaxonomyManager.class.getName());
        Taxonomy taxonomy = taxonomyManager.getTaxonomies().getTaxonomy(HippoBeanHelper.PUBLICATION_TAXONOMY);
        TaxonomyFacetWrapper taxonomyWrapper = hippoFacetNavigationBean == null ? null : new TaxonomyFacetWrapper(taxonomy, hippoFacetNavigationBean);

        request.setAttribute("taxonomy", taxonomyWrapper);
        request.setAttribute("query", queryParam);
        request.setModel("facets", hippoFacetNavigationBean);
        request.setAttribute("cparam", paramInfo);
    }

    protected HippoFacetNavigationBean getFacetNavigationBean(HstRequestContext context, String path, String query) {
        if (Strings.isNullOrEmpty(path)) {
            log.warn("Facetpath was empty {}", path);
            return null;
        } else {
            ResolvedSiteMapItem resolvedSiteMapItem = context.getResolvedSiteMapItem();
            String resolvedContentPath = PathUtils.normalizePath(resolvedSiteMapItem.getRelativeContentPath());
            String parsedQuery = this.cleanupSearchQuery(query);
            HippoFacetNavigationBean facNavBean;
            if (!StringUtils.isBlank(resolvedContentPath) && !resolvedContentPath.startsWith("/")
                && context.getSiteContentBaseBean().getBean(resolvedContentPath, HippoFacetNavigationBean.class) != null) {
                facNavBean = ContentBeanUtils.getFacetNavigationBean(resolvedContentPath, parsedQuery);
            } else {
                facNavBean = ContentBeanUtils.getFacetNavigationBean(path, parsedQuery);
            }

            return facNavBean;
        }
    }
}