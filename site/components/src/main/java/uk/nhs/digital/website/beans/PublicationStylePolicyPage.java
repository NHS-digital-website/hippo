package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:pubstylepolicypage")
@Node(jcrType = "website:pubstylepolicypage")
public class PublicationStylePolicyPage extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:pubstyle")
    public String getPublicationStyle() {
        return getSingleProperty("website:pubstyle");
    }

    @HippoEssentialsGenerated(internalName = "website:bannerimage")
    public CorporateWebsiteImageset getBannerImage() {
        return getLinkedBean("website:bannerimage", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:banneralttext")
    public String getImageAltText() {
        return getSingleProperty("website:banneralttext");
    }

}
