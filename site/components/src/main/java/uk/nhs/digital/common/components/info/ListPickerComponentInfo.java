package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;
import org.onehippo.cms7.essentials.components.info.EssentialsPageable;

public interface ListPickerComponentInfo extends EssentialsPageable {
    String HIPPO_DOCUMENT = "hippo:document";

    @Parameter(name = "document1")
    @JcrPath(
        isRelative = true,
        pickerSelectableNodeTypes = {HIPPO_DOCUMENT}
    )
    String getDocumentItem1();

    @Parameter(name = "document2")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getDocumentItem2();

    @Parameter(name = "document3")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getDocumentItem3();

    @Parameter(name = "document4")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getDocumentItem4();

    @Parameter(name = "document5")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getDocumentItem5();

    @Parameter(name = "document6")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getDocumentItem6();

    @Parameter(name = "document7")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getDocumentItem7();

    @Parameter(name = "document8")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getDocumentItem8();

    @Parameter(name = "document9")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getDocumentItem9();

    @Parameter(name = "document10")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getDocumentItem10();
}
