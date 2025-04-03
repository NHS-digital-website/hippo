package uk.nhs.digital.common.components.catalogue;

import static org.mockito.Mockito.mock;

import org.hippoecm.hst.core.component.HstRequest;
import uk.nhs.digital.common.components.info.CatalogueResultsComponentInfo;

public class CatalogueResultsComponentTestSubclass extends CatalogueResultsComponent {

    @Override
    public String getSearchQuery(HstRequest request) {
        return "testSearch";
    }

}
