package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;

public interface PublicationFeedHubComponentInfo extends EssentialsListComponentInfo {

    @Parameter(
        name = "path",
        defaultValue = "/content/documents/corporate-website/publication-system/valid-publication-series",
        required = true
        )
    @Override
    String getPath();

    @Parameter(
        name = "documentTypes",
        required = true,
        defaultValue = "publicationsystem:publication"
        )
    @Override
    String getDocumentTypes();

    @Parameter(
        name = "sortField",
        defaultValue = "publicationsystem:NominalDate"
        )
    @Override
    String getSortField();

    @Parameter(
        name = "sortOrder",
        defaultValue = "asc"
        )
    @DropDownList({"asc", "desc"})
    String getSortOrder();
}
