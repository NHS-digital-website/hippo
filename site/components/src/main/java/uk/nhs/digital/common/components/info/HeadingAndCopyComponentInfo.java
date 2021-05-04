package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.JcrPath;

public interface HeadingAndCopyComponentInfo {


    @JcrPath
    String getSourceDocument();

    @DropDownList({"center", "left"})
    String getAlignment();
}
