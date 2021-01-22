package uk.nhs.digital.common.components.info;


import org.hippoecm.hst.core.parameters.*;
import org.onehippo.cms7.essentials.components.info.*;

@FieldGroupList({
    @FieldGroup(
        titleKey = "Wrapper",
        value = {
            "wrappingDocument",
        }
    )
    })
public interface WrappedListPickerComponentInfo extends ListPickerComponentInfo {
    @Parameter(name = "wrappingDocument", displayName = "Wrapping document", required = false)
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getWrappingDocument();

    @Parameter(
        name = "showIcon",
        defaultValue = "true",
        required = false,
        displayName = "Show Icons (if supported in template)"
    )
    Boolean getShowIcon();
}
