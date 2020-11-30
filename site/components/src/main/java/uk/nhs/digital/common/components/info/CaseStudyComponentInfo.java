package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.*;

@FieldGroupList({
    @FieldGroup(
        titleKey = "CTA Document",
        value = {
            "document",
        }
    )
    })
public interface CaseStudyComponentInfo {

    @Parameter(
        name = "document",
        required = true,
        displayName = "CTA Document"
        )
    @JcrPath(
        isRelative = true,
        pickerSelectableNodeTypes = {"website:calltoaction"}
        )
    String getDocument();

    @Parameter(
        name = "alignment",
        required = false,
        defaultValue = "left",
        displayName = "Alignment of text"
        )
    @DropDownList({"left", "right"})
    String getTextAlignment();
}
