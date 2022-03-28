package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.*;

@FieldGroupList({
    @FieldGroup(
        titleKey = "Section",
        value = {"header", "introduction"}
    ),
    @FieldGroup(
        titleKey = "Template",
        value = {"template"}
    ),
    @FieldGroup(
        titleKey = "Navigation Button 1",
        value = {"button1Title", "button1Link", "buttonIconLink"}
    ),
    @FieldGroup(
        titleKey = "Navigation Button 2",
        value = {"button2Title", "button2Link", "buttonIconLink2"}
    ),
    @FieldGroup(
        titleKey = "Document",
        value = {"Card1","Card2","Card3","Card4","Card5","Card6","Card7","Card8","Card9","Card10","Card11","Card12"}
    )
    })
public interface CardListComponentInfo {
    String HIPPO_DOCUMENT = "hippo:document";

    @Parameter(
        name = "header",
        displayName = "Section header"
    )
    String getHeader();

    @Parameter(
        name = "introduction",
        displayName = "Section introduction"
    )
    String getIntroduction();

    @Parameter(
        name = "button1Title",
        displayName = "Navigation button 1 label"
    )
    String getButton1Title();

    @Parameter(
        name = "button1Link",
        displayName = "Navigation button 1 destination"
    )
    String getButton1Link();

    @Parameter(
        name = "button2Title",
        displayName = "Navigation button 2 label"
    )
    String getButton2Title();

    @Parameter(
        name = "button2Link",
        displayName = "Navigation button 2 destination"
    )
    String getButton2Link();

    @Parameter(
        name = "buttonIconLink",
        displayName = "get image link"
    )
    String getButtonIconLink();

    @Parameter(
        name = "buttonIconLink2",
        displayName = "get image link2"
    )
    String getButtonIconLink2();

    @Parameter(
        name = "template",
        required = false,
        defaultValue = "Default",
        displayName = "Template"
        )
    @DropDownList({"Default", "Grey", "MultiColor"})
    String getTemplate();

    @Parameter(name = "Card1")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard1();

    @Parameter(name = "Card2")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard2();

    @Parameter(name = "Card3")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard3();

    @Parameter(name = "Card4")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard4();

    @Parameter(name = "Card5")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard5();

    @Parameter(name = "Card6")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard6();

    @Parameter(name = "Card7")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard7();

    @Parameter(name = "Card8")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard8();

    @Parameter(name = "Card9")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard9();

    @Parameter(name = "Card10")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard10();

    @Parameter(name = "Card11")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard11();

    @Parameter(name = "Card12")
    @JcrPath(isRelative = true, pickerSelectableNodeTypes = {HIPPO_DOCUMENT})
    String getCard12();

}
