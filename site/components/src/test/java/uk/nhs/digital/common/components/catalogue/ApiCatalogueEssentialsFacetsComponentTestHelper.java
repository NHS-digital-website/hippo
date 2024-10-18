package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.component.HstRequest;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;

/**
 * This is a junit helper class to set the mock object for the class 'EssentialsFacetsComponentInfo'.
 * As 'getComponentParametersInfo()' is of protected access type, overriding this method in this class to get
 * the mock value for 'paramInfo.getFacetPath()' in EssentialsFacetsComponent.
 * This is written for the method 'super.doBeforeRender(request, response);' in ApiCatalogueEssentialsFacetsComponent.
 */
class ApiCatalogueEssentialsFacetsComponentTestHelper extends ApiCatalogueEssentialsFacetsComponent {

    private EssentialsFacetsComponentInfo mockInfo;

    public ApiCatalogueEssentialsFacetsComponentTestHelper(EssentialsFacetsComponentInfo info) {
        this.mockInfo = info;
    }

    @Override
    protected EssentialsFacetsComponentInfo getComponentParametersInfo(HstRequest request) {
        return mockInfo; // Return the mocked info instead of the real implementation
    }

}

