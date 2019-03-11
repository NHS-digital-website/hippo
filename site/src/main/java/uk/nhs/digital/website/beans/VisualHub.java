package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:visualhub")
@Node(jcrType = "website:visualhub")
public class VisualHub extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:image")
    public HippoGalleryImageSet getImage() {
        return getLinkedBean("website:image", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:icon")
    public HippoGalleryImageSet getIcon() {
        return getLinkedBean("website:icon", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:links")
    public List<HippoBean> getLinks() {
        return getChildBeansByName("website:links");
    }

    @HippoEssentialsGenerated(internalName = "website:additionalinformation")
    public HippoHtml getAdditionalInformation() {
        return getHippoHtml("website:additionalinformation");
    }

}
