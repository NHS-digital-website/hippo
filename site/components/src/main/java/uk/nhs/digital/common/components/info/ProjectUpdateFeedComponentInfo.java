package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsListComponentInfo;

public interface ProjectUpdateFeedComponentInfo extends EssentialsListComponentInfo {
    @Parameter(
        name = "documentTypes",
        required = true,
        defaultValue = "website:projectupdate"
        )
    @Override
    String getDocumentTypes();

    @Parameter(
        name = "sortField",
        defaultValue = "website:updatetimestamp"
        )
    @Override
    String getSortField();

    @Parameter(
        name = "sortOrder",
        defaultValue = "desc"
        )
    @DropDownList({"asc", "desc"})
    String getSortOrder();
}
