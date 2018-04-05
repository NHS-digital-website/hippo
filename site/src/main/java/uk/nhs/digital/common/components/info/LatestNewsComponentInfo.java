package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.*;
import org.onehippo.cms7.essentials.components.info.*;

public interface LatestNewsComponentInfo extends EssentialsNewsComponentInfo {

    @Parameter(
        name = "pageSize",
        required = true,
        defaultValue = "2",
        description = "Nr of items per page"
        )
    int getPageSize();

    @Parameter(
        name = "path",
        required = false
        )
    @JcrPath(
        pickerConfiguration = "cms-pickers/documents",
        pickerSelectableNodeTypes = {"hippostd:folder"},
        pickerInitialPath = "/content/documents/corporate-website/news"
        )
    @Override
    String getPath();

    @Parameter(
        name = "documentTypes",
        required = true,
        defaultValue = "website:news"
        )
    @Override
    String getDocumentTypes();

    @Parameter(
        name = "documentDateField",
        required = false,
        defaultValue = "website:publisheddatetime"
    )
    String getDocumentDateField();

    @Parameter(
        name = "sortField",
        required = false,
        defaultValue = "website:publisheddatetime"
        )
    @Override
    String getSortField();
}
