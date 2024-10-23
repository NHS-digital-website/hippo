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

    @Parameter(
        name = "TaxonomyStatusGroupName",
        required = true,
        displayName = "The label used in the Taxonomy Filter Mapping Document used to denote status"
    )
    String getTaxonomyStatusGroupName();

}
