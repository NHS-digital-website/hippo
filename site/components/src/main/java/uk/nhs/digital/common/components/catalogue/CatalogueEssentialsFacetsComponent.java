package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.component.HstRequest;
import org.hippoecm.hst.core.component.HstResponse;
import org.hippoecm.hst.core.parameters.ParametersInfo;
import org.onehippo.cms7.essentials.components.EssentialsFacetsComponent;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;

@ParametersInfo(
        type = EssentialsFacetsComponentInfo.class
)
public class CatalogueEssentialsFacetsComponent extends EssentialsFacetsComponent {
    @Override
    public void doBeforeRender(HstRequest request, HstResponse response) {
        super.doBeforeRender(request, response);
        FacetNavHelper facetsNavHelper = new FacetNavHelperImpl(request.getModel("facets"));
        request.getRequestContext().setModel("facetsNavHelper", facetsNavHelper);
    }
}
