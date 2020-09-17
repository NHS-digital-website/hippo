package uk.nhs.digital.intranet.components.info;

import org.hippoecm.hst.core.parameters.FieldGroup;
import org.hippoecm.hst.core.parameters.FieldGroupList;
import org.hippoecm.hst.core.parameters.Parameter;

@FieldGroupList({
    @FieldGroup(
        value = {
            "headline",
            "buttonText",
            "buttonUrl"
        },
        titleKey = "Join the Conversation")
    })
public interface JoinTheConversationComponentInfo {

    @Parameter(name = "headline", displayName = "Headline")
    String getHeadline();

    @Parameter(name = "buttonText", displayName = "Button Text")
    String getButtonText();

    @Parameter(name = "buttonUrl", displayName = "Button Url")
    String getButtonUrl();
}
