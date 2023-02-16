package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;

public interface SeriesFeedHubComponentInfo extends EssentialsListComponentInfo {

    @Parameter(
        name = "pageSize",
        required = true,
        defaultValue = "10",
        displayName = "Number of items per page"
        )
    int getPageSize();

    @Parameter(
        name = "path",
        required = false
        )
    @JcrPath(
        pickerConfiguration = "cms-pickers/documents",
        pickerSelectableNodeTypes = {"hippostd:folder"},
        pickerInitialPath = "/content/documents/corporate-website/publication-system"
        )
    @Override
    String getPath();

    @Parameter(
        name = "documentTypes",
        required = true,
        defaultValue = "publicationsystem:series"
        )
    @Override
    String getDocumentTypes();

    @Parameter(
        name = "documentDateField",
        required = false,
        defaultValue = "publicationsystem:NominalDate"
    )
    String getDocumentDateField();

    @Parameter(
        name = "sortField",
        required = false,
        defaultValue = "publicationsystem:NominalDate"
        )
    @Override
    String getSortField();
}
