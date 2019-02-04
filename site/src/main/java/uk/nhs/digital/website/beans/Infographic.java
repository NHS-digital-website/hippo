package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:infographic")
@Node(jcrType = "website:infographic")
public class Infographic extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:colour")
    public String getColour() {
        return getProperty("website:colour");
    }

    @HippoEssentialsGenerated(internalName = "website:headline")
    public String getHeadline() {
        return getProperty("website:headline");
    }

    @HippoEssentialsGenerated(internalName = "website:explanatoryLine")
    public HippoHtml getExplanatoryLine() {
        return getHippoHtml("website:explanatoryLine");
    }

    @HippoEssentialsGenerated(internalName = "website:icon")
    public HippoGalleryImageSet getIcon() {
        return getLinkedBean("website:icon", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:qualifyingInformation")
    public HippoHtml getQualifyingInformation() {
        return getHippoHtml("website:qualifyingInformation");
    }
}
