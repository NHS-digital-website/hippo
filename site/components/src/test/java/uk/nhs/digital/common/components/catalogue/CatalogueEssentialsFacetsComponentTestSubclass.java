package uk.nhs.digital.common.components.catalogue;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hippoecm.hst.core.component.HstRequest;
import uk.nhs.digital.common.components.info.CatalogueEssentialsFacetsComponentInfo;

public class CatalogueEssentialsFacetsComponentTestSubclass extends CatalogueEssentialsFacetsComponent {

    @Override
    protected CatalogueEssentialsFacetsComponentInfo getComponentParametersInfo(HstRequest request) {
        // Return a mock object for testing purposes
        CatalogueEssentialsFacetsComponentInfo paramInfo = mock(CatalogueEssentialsFacetsComponentInfo.class);
        when(paramInfo.getTaxonomyFilterMappingDocumentPath()).thenReturn("mocked/taxonomy/path");
        return paramInfo;
    }
}
