package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.FieldGroup;
import org.hippoecm.hst.core.parameters.FieldGroupList;
import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;

@FieldGroupList({
    @FieldGroup(
        titleKey = "Call To Action Document",
        value = {
            "document",
        }
    )
    })

public interface CallToActionComponentInfo {
    @Parameter(name = "document", displayName = "Call to Action Document", required = true)
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {"website:calltoaction"})
    String getDocument();
}
