package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.ArrayList;
import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:cilandingasset")
@Node(jcrType = "publicationsystem:cilandingasset")
public class CiLandingAsset extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:content")
    public HippoHtml getContent() {
        return getHippoHtml("publicationsystem:content");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Attachments")
    public List<HippoCompound> getAttachments() {
        // Support old and new style attachments
        List<HippoCompound> attachments = new ArrayList<>();
        attachments.addAll(getChildBeansByName("publicationsystem:Attachments", Attachment.class));
        attachments.addAll(getChildBeansByName("publicationsystem:Attachments", ExtAttachment.class));
        return attachments;
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:relatedlink")
    public List<RelatedLink> getRelatedLinks() {
        return getChildBeansByName("publicationsystem:relatedlink", RelatedLink.class);
    }
}
