package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsFacetsComponentInfo;

public interface CatalogueEssentialsFacetsComponentInfo extends EssentialsFacetsComponentInfo {

    @Parameter(
        name = "TaxonomyFiltersMappingDocumentPath",
        required = true,
        displayName = "Taxonomy Filter Mapping Document Path"
    )
    String getTaxonomyFilterMappingDocumentPath();

}