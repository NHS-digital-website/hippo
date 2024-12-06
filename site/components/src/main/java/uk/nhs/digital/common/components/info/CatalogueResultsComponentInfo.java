package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;

public interface CatalogueResultsComponentInfo extends EssentialsListComponentInfo {

    @Parameter(
        name = "taxonomyFiltersMappingDocumentPath",
        required = true,
        displayName = "Taxonomy Filter Mapping Document Path"
    )
    String getTaxonomyFilterMappingDocumentPath();

    @Parameter(
        name = "facetsName",
        required = true,
        displayName = "The name of the facets used"
    )
    String getFacetsName();

}
