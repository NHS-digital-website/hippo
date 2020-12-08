package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:galleryitem")
@Node(jcrType = "website:galleryitem")
public class GalleryItem extends BaseCompound {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:image")
    public CorporateWebsiteImageset getImage() {
        return getLinkedBean("website:image", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:imagealt")
    public String getImageAlt() {
        return getSingleProperty("website:imagealt");
    }

    @HippoEssentialsGenerated(internalName = "website:imagewarning")
    public String getImageWarning() {
        return getSingleProperty("website:imagewarning");
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:relatedfiles")
    public List<HippoBean> getRelatedFiles() {
        return getChildBeansByName("website:relatedfiles");
    }

}
