package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.DropDownList;
import org.hippoecm.hst.core.parameters.Parameter;

public interface ButtonComponentInfo {
    @Parameter(
        name = "header",
        displayName = "Heading"
    )
    String getHeader();

    @Parameter(
        name = "button1Title",
        displayName = "Button 1 title"
    )
    String getButton1Title();

    @Parameter(
        name = "button1Link",
        displayName = "Button 1 link"
    )
    String getButton1Link();

    @Parameter(
        name = "button2Title",
        displayName = "Button 2 title"
    )
    String getButton2Title();

    @Parameter(
        name = "button2Link",
        displayName = "Button 2 link"
    )
    String getButton2Link();

    @Parameter(
        name = "button3Title",
        displayName = "Button 3 title"
    )
    String getButton3Title();

    @Parameter(
        name = "button3Link",
        displayName = "Button 3 link"
    )
    String getButton3Link();

    @Parameter(
        name = "alignment",
        defaultValue = "left"
        )

    @DropDownList({"center", "left"})
    String getAlignment();
}
