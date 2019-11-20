package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoResource;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "publicationsystem:attachment")
@Node(jcrType = "publicationsystem:attachment")
public class Attachment extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "publicationsystem:displayName")
    public String getText() {
        String text = getProperty("publicationsystem:displayName");
        if (text == null || text.isEmpty()) {
            text = getResource().getFilename();
        }
        return text;
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:attachmentResource")
    public HippoResource getResource() {
        return getChildBeansByName("publicationsystem:attachmentResource", HippoResource.class).get(0);
    }
}
