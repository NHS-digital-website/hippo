package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:fullWidthImage")
@Node(jcrType = "website:fullWidthImage")
public class FullWidthImage extends HippoCompound {

    public String getSectionType() {
        return "fullWidthImage";
    }

    @HippoEssentialsGenerated(internalName = "website:largeImage")
    public HippoGalleryImageSet getLargeImage() {
        return getLinkedBean("website:largeImage", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:smallImage")
    public HippoGalleryImageSet getSmallImage() {
        return getLinkedBean("website:smallImage", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:altText")
    public String getAltText() {
        return getProperty("website:altText");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }
}
