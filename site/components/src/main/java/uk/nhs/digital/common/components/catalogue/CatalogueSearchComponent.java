package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.CommonComponent;
import org.onehippo.cms7.essentials.components.utils.SiteUtils;
import uk.nhs.digital.common.components.info.CatalogueSearchComponentInfo;

@ParametersInfo(
    type = CatalogueSearchComponentInfo.class
)
public class CatalogueSearchComponent extends CommonComponent {

    @Override
    public void doBeforeRender(final HstRequest request, final HstResponse response) {
        super.doBeforeRender(request, response);
        request.setAttribute("query", SiteUtils.getAnyParameter("query", request, this));

        CatalogueSearchComponentInfo info = getComponentParametersInfo(request);
        request.setAttribute("catalogueSitemapRefId", info.getCatalogueSitemapRefId());
    }
}
