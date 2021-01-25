package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.*;

public interface WhiteSpaceComponentInfo {
    @Parameter(
        name = "size",
        displayName = "Size",
        defaultValue = "medium",
        required = true
        )
    @DropDownList({"small", "medium", "large"})
    String getSize();
}
