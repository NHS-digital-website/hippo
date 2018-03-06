package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

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
    public List<Attachment> getAttachments() {
        return getChildBeansByName("publicationsystem:Attachments", Attachment.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:relatedlink")
    public List<RelatedLink> getRelatedLinks() {
        return getChildBeansByName("publicationsystem:relatedlink", RelatedLink.class);
    }
}
