package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsDocumentComponentInfo;


public interface TargetedBannerComponentInfo extends EssentialsDocumentComponentInfo {
    @Parameter(name = "document", displayName = "Targeted Banner Document", required = true)
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {"website:targetedbanner"})
    String getDocument();
}
