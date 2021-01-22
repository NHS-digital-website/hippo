package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;

public interface LatestBlogComponentInfo extends EssentialsListComponentInfo {

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
        pickerInitialPath = "/content/documents/corporate-website/blogs"
        )
    @Override
    String getPath();

    @Parameter(
        name = "documentTypes",
        required = true,
        defaultValue = "website:blog"
        )
    @Override
    String getDocumentTypes();

    @Parameter(
        name = "documentDateField",
        required = false,
        defaultValue = "website:dateofpublication"
    )
    String getDocumentDateField();

    @Parameter(
        name = "sortField",
        required = false,
        defaultValue = "website:dateofpublication"
        )
    @Override
    String getSortField();
}
