package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:ImageModule")
@Node(jcrType = "website:ImageModule")
public class ImageModule extends HippoCompound {

    public String getSectionType() {
        return "imageModule";
    }

    @HippoEssentialsGenerated(internalName = "website:imageType")
    public String getImageType() {
        return getSingleProperty("website:imageType");
    }

    @HippoEssentialsGenerated(internalName = "website:image")
    public HippoGalleryImageSet getImage() {
        System.out.println("XXXXXXXXXXXXXX IN 1 XXXXXXXXXXXXXXXXXX\n");
        return getLinkedBean("website:image", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:altText")
    public String getAltText() {
        return getSingleProperty("website:altText");
    }

    @HippoEssentialsGenerated(internalName = "website:caption")
    public String getCaption() {
        return getSingleProperty("website:caption");
    }

    @HippoEssentialsGenerated(internalName = "website:text")
    public String getText() {
        return getSingleProperty("website:text");
    }
}
