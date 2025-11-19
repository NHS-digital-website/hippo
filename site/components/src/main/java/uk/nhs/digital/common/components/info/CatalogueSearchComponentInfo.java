package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.Parameter;
import org.hippoecm.hst.core.parameters.ParametersInfo;

@ParametersInfo(type = CatalogueSearchComponentInfo.class)
public interface CatalogueSearchComponentInfo {

    @Parameter(
        name = "catalogueSitemapRefId",
        required = true,
        defaultValue = "catalogue",
        displayName = "Catalogue sitemap-item refId"
    )
    String getCatalogueSitemapRefId();
}
