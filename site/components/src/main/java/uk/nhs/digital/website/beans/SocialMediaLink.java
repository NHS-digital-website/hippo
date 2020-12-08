package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@Node(jcrType = "website:socialmedialink")
public class SocialMediaLink extends CommonFieldsBean {
    @HippoEssentialsGenerated(internalName = "website:linkname")
    public String getLinkName() {
        return getSingleProperty("website:linkname");
    }

    @HippoEssentialsGenerated(internalName = "website:sitemainurl")
    public String getSiteMainUrl() {
        return getSingleProperty("website:sitemainurl");
    }

    @HippoEssentialsGenerated(internalName = "website:nhsdigitalurl")
    public String getNhsDigitalUrl() {
        return getSingleProperty("website:nhsdigitalurl");
    }

    @HippoEssentialsGenerated(internalName = "website:linkicon")
    public HippoGalleryImageSet getLinkIcon() {
        return getLinkedBean("website:linkicon", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:notes")
    public String getNotes() {
        return getSingleProperty("website:notes");
    }
}
