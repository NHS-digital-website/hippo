package uk.nhs.digital.common.components.info;

import org.hippoecm.hst.core.parameters.FieldGroup;
import org.hippoecm.hst.core.parameters.FieldGroupList;
import org.hippoecm.hst.core.parameters.JcrPath;
import org.hippoecm.hst.core.parameters.Parameter;

@FieldGroupList({@FieldGroup(
    value = {"title"}
    ), @FieldGroup(
    titleKey = "Social Media Documents",
    value = {"social1", "social2", "social3", "social4", "social5", "social6", "social7", "social8", "social9", "social10"}
)})
public interface SocialMediaComponentInfo {
    @Parameter(
        name = "title",
        displayName = "Title",
        defaultValue = "Follow us on social media",
        required = true
    )
    String getTitle();

    @Parameter(
        name = "social1",
        displayName = "Social Media 1",
        required = true
        )
    @JcrPath(
        pickerSelectableNodeTypes = {"website:socialmedialink"},
        pickerInitialPath = "/content/documents",
        isRelative = true
        )
    String getSocialMedia1();

    @Parameter(
        name = "social2",
        displayName = "Social Media 2"
        )
    @JcrPath(
        pickerSelectableNodeTypes = {"website:socialmedialink"},
        pickerInitialPath = "/content/documents",
        isRelative = true
        )
    String getSocialMedia2();

    @Parameter(
        name = "social3",
        displayName = "Social Media 3"
        )
    @JcrPath(
        pickerSelectableNodeTypes = {"website:socialmedialink"},
        pickerInitialPath = "/content/documents",
        isRelative = true
        )
    String getSocialMedia3();

    @Parameter(
        name = "social4",
        displayName = "Social Media 4"
        )
    @JcrPath(
        pickerSelectableNodeTypes = {"website:socialmedialink"},
        pickerInitialPath = "/content/documents",
        isRelative = true
    )
    String getSocialMedia4();

    @Parameter(
        name = "social5",
        displayName = "Social Media 5"
        )
    @JcrPath(
        pickerSelectableNodeTypes = {"website:socialmedialink"},
        pickerInitialPath = "/content/documents",
        isRelative = true
        )
    String getSocialMedia5();

    @Parameter(
        name = "social6",
        displayName = "Social Media 6"
        )
    @JcrPath(
        pickerSelectableNodeTypes = {"website:socialmedialink"},
        pickerInitialPath = "/content/documents",
        isRelative = true
    )
    String getSocialMedia6();

    @Parameter(
        name = "social7",
        displayName = "Social Media 7"
        )
    @JcrPath(
        pickerSelectableNodeTypes = {"website:socialmedialink"},
        pickerInitialPath = "/content/documents",
        isRelative = true
        )
    String getSocialMedia7();

    @Parameter(
        name = "social8",
        displayName = "Social Media 8"
        )
    @JcrPath(
        pickerSelectableNodeTypes = {"website:socialmedialink"},
        pickerInitialPath = "/content/documents",
        isRelative = true
        )
    String getSocialMedia8();

    @Parameter(
        name = "social9",
        displayName = "Social Media 9"
        )
    @JcrPath(
        pickerSelectableNodeTypes = {"website:socialmedialink"},
        pickerInitialPath = "/content/documents",
        isRelative = true
        )
    String getSocialMedia9();

    @Parameter(
        name = "social10",
        displayName = "Social Media 10"
        )
    @JcrPath(
        pickerSelectableNodeTypes = {"website:socialmedialink"},
        pickerInitialPath = "/content/documents",
        isRelative = true
        )
    String getSocialMedia10();
}
