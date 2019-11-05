package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:socialmediaimages")
@Node(jcrType = "website:socialmediaimages")
public class SocialMediaImages extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:socialmediaimage", allowModifications = false)
    public HippoGalleryImageSet getSocialmediaimage() {
        return getLinkedBean("website:socialmediaimage", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:twitterimage", allowModifications = false)
    public HippoGalleryImageSet getTwitterimage() {
        return getLinkedBean("website:twitterimage", HippoGalleryImageSet.class);
    }

}
