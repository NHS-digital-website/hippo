package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.parameters.ParametersInfo;
import uk.nhs.digital.common.components.info.ApiCatalogueHubComponentInfo;

@ParametersInfo(
    type = ApiCatalogueHubComponentInfo.class
)
public class CatalogueResultsComponent extends ApiCatalogueHubComponent {

    public static final String TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH = "/content/documents/administration/website/service-catalogue/taxonomy-filters-mapping";

    @Override
    protected CatalogueFilterManager getFilterManager() {
        return new CatalogueFilterManager(TAXONOMY_FILTERS_MAPPING_DOCUMENT_PATH);
    }
}


