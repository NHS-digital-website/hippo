package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:socialmediaimages")
@Node(jcrType = "website:socialmediaimages")
public class SocialMediaImages extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:socialmediaimage", allowModifications = false)
    public CorporateWebsiteImageset getSocialmediaimage() {
        return getLinkedBean("website:socialmediaimage", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:twitterimage", allowModifications = false)
    public CorporateWebsiteImageset getTwitterimage() {
        return getLinkedBean("website:twitterimage", CorporateWebsiteImageset.class);
    }

}
