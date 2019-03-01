package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:galleryitem")
@Node(jcrType = "website:galleryitem")
public class GalleryItem extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:image")
    public HippoGalleryImageSet getImage() {
        return getLinkedBean("website:image", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:imagewarning")
    public String getImageWarning() {
        return getProperty("website:imagewarning");
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
