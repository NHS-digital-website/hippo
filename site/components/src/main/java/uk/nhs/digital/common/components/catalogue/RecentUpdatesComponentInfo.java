package uk.nhs.digital.common.components.catalogue;

import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;

public interface RecentUpdatesComponentInfo extends EssentialsListComponentInfo {
    @Parameter(
            name = "pageSize",
            required = true,
            defaultValue = "15",
            displayName = "Nr of items per page"
    )
    int getPageSize();

    @Parameter(
            name = "path",
            required = false
    )
    @JcrPath(
            pickerConfiguration = "cms-pickers/documents",
            pickerSelectableNodeTypes = {"hippostd:folder"},
            pickerInitialPath = "/content/documents/corporate-website/services"
    )
    @Override
    String getPath();

    @Parameter(
            name = "documentTypes",
            required = true,
            defaultValue = "website:service"
    )
    @Override
    String getDocumentTypes();

    @Parameter(
            name = "publisheddate",
            required = false,
            defaultValue = "publisheddate"
    )
    String getPublishedDate();

    @Parameter(
            name = "sortField",
            required = false,
            defaultValue = "publisheddate"
    )
    @Override
    String getSortField();
}
