package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:personimage")
@Node(jcrType = "website:personimage")
public class PersonImage extends HippoCompound {

    public String getSectionType() {
        return "personimage";
    }

    @HippoEssentialsGenerated(internalName = "website:picture")
    public HippoGalleryImageSet getPicture() {
        return getLinkedBean("website:picture", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:imagesourcepermission")
    public String getImagesourcepermission() {
        return getProperty("website:imagesourcepermission");
    }

    @HippoEssentialsGenerated(internalName = "website:imagerightsdetails")
    public HippoHtml getImagerightsdetails() {
        return getHippoHtml("website:imagerightsdetails");
    }

    @HippoEssentialsGenerated(internalName = "website:imagedistributiontagging")
    public String getImagedistributiontagging() {
        return getProperty("website:imagedistributiontagging");
    }

    @HippoEssentialsGenerated(internalName = "website:otherimagedistributiontagging")
    public String getOtherimagesourcepermission() {
        return getProperty("website:otherimagedistributiontagging");
    }
}
