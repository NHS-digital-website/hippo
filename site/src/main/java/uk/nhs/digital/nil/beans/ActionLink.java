package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:actionlink")
@Node(jcrType = "nationalindicatorlibrary:actionlink")
public class ActionLink extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:actionLinkTitle")
    public String getLinkText() {
        return getProperty("nationalindicatorlibrary:actionLinkTitle", getLinkUrl());
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:actionLinkUrl")
    public String getLinkUrl() {
        return getProperty("nationalindicatorlibrary:actionLinkUrl");
    }
}
