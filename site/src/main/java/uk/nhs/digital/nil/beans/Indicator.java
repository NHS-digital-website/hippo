package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import uk.nhs.digital.ps.beans.Attachment;

import java.util.List;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:indicator")
@Node(jcrType = "nationalindicatorlibrary:indicator")
public class Indicator extends BaseDocument {
    @HippoEssentialsGenerated(internalName = PropertyKeys.TOPBAR)
    public Topbar getTopbar() {
        return getBean(PropertyKeys.TOPBAR, Topbar.class);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.DETAILS)
    public Details getDetails() {
        return getBean(PropertyKeys.DETAILS, Details.class);
    }

    @HippoEssentialsGenerated(internalName = PropertyKeys.ATTACHMENTS)
    public List<Attachment> getAttachments() {
        return getChildBeansByName(PropertyKeys.ATTACHMENTS, Attachment.class);
    }

    interface PropertyKeys {
        String ATTACHMENTS = "nationalindicatorlibrary:attachments";
        String DETAILS = "nationalindicatorlibrary:details";
        String TOPBAR = "nationalindicatorlibrary:topbar";
    }
}
