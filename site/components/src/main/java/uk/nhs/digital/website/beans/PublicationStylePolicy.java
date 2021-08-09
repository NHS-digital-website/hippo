package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:pubstylesection")
@Node(jcrType = "website:pubstylesection")
public class PublicationStylePolicy extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:publicationStyle")
    public String getPublicationStyle() {
        return getSingleProperty("website:publicationStyle");
    }

    @HippoEssentialsGenerated(internalName = "website:button")
    public String getButton() {
        return getSingleProperty("website:button");
    }

    @HippoEssentialsGenerated(internalName = "website:banneralttext")
    public String getImageAltText() {
        return getSingleProperty("website:banneralttext");
    }

    @HippoEssentialsGenerated(internalName = "website:bannerimage")
    public CorporateWebsiteImageset getBannerImage() {
        return getLinkedBean("website:bannerimage", CorporateWebsiteImageset.class);
    }
}
