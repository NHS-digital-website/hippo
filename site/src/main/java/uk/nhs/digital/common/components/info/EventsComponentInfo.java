package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.*;
import org.onehippo.cms7.essentials.components.info.*;

public interface EventsComponentInfo extends EssentialsEventsComponentInfo {

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
        pickerInitialPath = "/content/documents/corporate-website/events"
        )
    @Override
    String getPath();

    @Parameter(
        name = "documentTypes",
        required = true,
        defaultValue = "website:event"
        )
    @Override
    String getDocumentTypes();

    @Parameter(
        name = "documentDateField",
        required = false,
        defaultValue = "website:startdatetime"
    )
    String getDocumentDateField();

    @Parameter(
        name = "sortField",
        required = false,
        defaultValue = "website:startdatetime"
        )
    @Override
    String getSortField();

    @Parameter(
        name = "sortOrder",
        required = false,
        defaultValue = "asc",
        description = "Order results ascending or descending"
        )
    @DropDownList({"asc", "desc"})
    String getSortOrder();

    @Parameter(name = "includeSubtypes", defaultValue = "true", required = false, hideInChannelManager = true)
    Boolean getIncludeSubtypes();
}
