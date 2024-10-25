package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;

public interface CatalogueResultsComponentInfo extends EssentialsListComponentInfo {

    @Parameter(
        name = "TaxonomyFiltersMappingDocumentPath",
        required = true,
        displayName = "Taxonomy Filter Mapping Document Path"
    )
    String getTaxonomyFilterMappingDocumentPath();

}
