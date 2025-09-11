package uk.nhs.digital.common.components.catalogue;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.core.request.HstRequestContext;
import org.hippoecm.hst.core.request.ResolvedSiteMapItem;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.hippoecm.hst.util.PathUtils;
import org.onehippo.cms7.essentials.components.EssentialsFacetsComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ParametersInfo(
    type = EssentialsFacetsComponentInfo.class
)
public class FilteredEssentialsFacetsComponent extends EssentialsFacetsComponent {

    private static final Logger log = LoggerFactory.getLogger(FilteredEssentialsFacetsComponent.class);

    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        EssentialsFacetsComponentInfo paramInfo = getComponentParametersInfo(request);
        String facetPath = paramInfo.getFacetPath();

        if (facetPath != null) {
            HippoFacetNavigationBean facetNavBean =
                request.getRequestContext()
                    .getSiteContentBaseBean()
                    .getBean(facetPath, HippoFacetNavigationBean.class);

            if (facetNavBean != null) {
                request.setAttribute("alphabetFacets", facetNavBean);
            }
        }
    }

    @Override
    protected HippoFacetNavigationBean getFacetNavigationBean(HstRequestContext context, String path, String query) {
        if (Strings.isNullOrEmpty(path)) {
            log.warn("Facetpath was empty {}", path);
            return null;
        } else {
            ResolvedSiteMapItem resolvedSiteMapItem = context.getResolvedSiteMapItem();
            String resolvedContentPath = PathUtils.normalizePath(resolvedSiteMapItem.getRelativeContentPath());
            String searchQuery = this.cleanupSearchQuery(query);

            if (searchQuery != null && !searchQuery.isEmpty()) {
                searchQuery = "xpath(//*[jcr:contains(@website:title,'" + searchQuery + "') or jcr:contains(@website:shortsummary,'" + searchQuery + "')])";
            }

            HippoFacetNavigationBean facNavBean;
            if (!StringUtils.isBlank(resolvedContentPath) && !resolvedContentPath.startsWith("/")
                && context.getSiteContentBaseBean().getBean(resolvedContentPath, HippoFacetNavigationBean.class) != null) {
                facNavBean = ContentBeanUtils.getFacetNavigationBean(resolvedContentPath, searchQuery);
            } else {
                facNavBean = ContentBeanUtils.getFacetNavigationBean(path, searchQuery);
            }

            return facNavBean;
        }
    }
}
