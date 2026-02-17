package uk.nhs.digital.common.components;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import uk.nhs.digital.common.components.info.CyberAlertsHubComponentInfo;

@ParametersInfo(
    type = CyberAlertsHubComponentInfo.class
)
public class CyberAlertsHubComponent extends EssentialsListComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        // Get the total results before the facet limit is applied.
        EssentialsListComponentInfo paramInfo = this.getComponentParametersInfo(request);
        HippoBean scope = this.getSearchScope(request, paramInfo.getPath());
        if (scope == null) {
            // No valid scope; avoid exceptions and default to 0 results
            request.setAttribute("totalAvailable", 0);
            return;
        }
        String relPath;
        try {
            relPath = SiteUtils.relativePathFrom(scope, request.getRequestContext());
        } catch (StringIndexOutOfBoundsException e) {
            request.setAttribute("totalAvailable", 0);
            return;
        }
        if (relPath == null || relPath.isEmpty()) {
            request.setAttribute("totalAvailable", 0);
            return;
        }
        HippoFacetNavigationBean facetNavigationBean = ContentBeanUtils.getFacetNavigationBean(relPath, this.getSearchQuery(request));
        request.setAttribute("totalAvailable",
            facetNavigationBean != null && facetNavigationBean.getCount() != null
                ? facetNavigationBean.getCount().intValue()
                : 0);
    }
}
