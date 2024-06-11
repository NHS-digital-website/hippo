package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoFacetNavigationBean;
import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.hippoecm.hst.util.ContentBeanUtils;
import org.onehippo.cms7.essentials.components.EssentialsListComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import uk.nhs.digital.common.components.info.ApiCatalogueHubComponentInfo;

@ParametersInfo(
    type = ApiCatalogueHubComponentInfo.class
)
public class ApiCatalogueHubComponent extends EssentialsListComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);

        // Get the total results before the facet limit is applied.
        EssentialsListComponentInfo paramInfo = this.getComponentParametersInfo(request);
        HippoBean scope = this.getSearchScope(request, paramInfo.getPath());
        String relPath = SiteUtils.relativePathFrom(scope, request.getRequestContext());
        HippoFacetNavigationBean facetNavigationBean = ContentBeanUtils.getFacetNavigationBean(relPath, this.getSearchQuery(request));
        request.setAttribute("totalAvailable", facetNavigationBean.getCount().intValue());
    }
}
