package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.component.HstRequest;
import uk.nhs.digital.common.components.info.CatalogueSearchComponentInfo;

public class CatalogueSearchComponentTest1 extends CatalogueSearchComponent {
    private CatalogueSearchComponentInfo mockInfo;

    public void setMockInfo(CatalogueSearchComponentInfo mockInfo) {
        this.mockInfo = mockInfo;
    }

    @Override
    protected CatalogueSearchComponentInfo getComponentParametersInfo(HstRequest request) {
        return mockInfo;
    }
}
