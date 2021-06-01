package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;

public interface HeadingAndCopyComponentInfo {


    @Parameter(name = "callToActionSource", displayName = "Call to Action")
    @JcrPath (isRelative = true, pickerSelectableNodeTypes = "website:calltoaction")
    String getSourceDocument();

    @Parameter(name = "alignment", displayName = "Alignment", defaultValue = "left")
    @DropDownList({"center", "left"})
    String getAlignment();
}
