package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.*;

public interface WhiteSpaceComponentInfo {
    @Parameter(
        name = "size",
        displayName = "Size",
        defaultValue = "Medium",
        required = true
        )
    @DropDownList({"Small", "Medium", "Large", "Extra Large"})
    String getSize();
}
