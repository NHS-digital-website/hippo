package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:directiontositebytrain")
@Node(jcrType = "website:directiontositebytrain")
public class DirectionToSiteByTrain extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:bytraintext")
    public HippoHtml getBytraintext() {
        return getHippoHtml("website:bytraintext");
    }

    @HippoEssentialsGenerated(internalName = "website:bytrainpicture")
    public HippoGalleryImageSet getBytrainpicture() {
        return getLinkedBean("website:bytrainpicture", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:bytrainvideo")
    public HippoHtml getBytrainvideo() {
        return getHippoHtml("website:bytrainvideo");
    }

    public String getSectionType() {
        return "directiontositebytrain";
    }

}
