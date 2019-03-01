package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:gallerysection")
@Node(jcrType = "website:gallerysection")
public class GallerySection extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:headinglevel")
    public String getHeadingLevel() {
        return getProperty("website:headinglevel");
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:galleryitems")
    public List<HippoBean> getGalleryItems() {
        return getChildBeansByName("website:galleryitems");
    }

    public String getSectionType() {
        return "gallerySection";
    }
    
}
