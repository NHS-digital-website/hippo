package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:fullWidthImage")
@Node(jcrType = "website:fullWidthImage")
public class FullWidthImage extends HippoCompound {

    public String getSectionType() {
        return "fullWidthImage";
    }

    @HippoEssentialsGenerated(internalName = "website:largeImage")
    public CorporateWebsiteImageset getLargeImage() {
        return getLinkedBean("website:largeImage", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:smallImage")
    public CorporateWebsiteImageset getSmallImage() {
        return getLinkedBean("website:smallImage", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:altText")
    public String getAltText() {
        return getSingleProperty("website:altText");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }
}
