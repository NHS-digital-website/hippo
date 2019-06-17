package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:directiontositebypublictransport")
@Node(jcrType = "website:directiontositebypublictransport")
public class DirectionToSiteByPublicTransport extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:bypublictransporttext")
    public HippoHtml getBypublictransporttext() {
        return getHippoHtml("website:bypublictransporttext");
    }

    @HippoEssentialsGenerated(internalName = "website:bypublictransportpicture")
    public HippoGalleryImageSet getBypublictransportpicture() {
        return getLinkedBean("website:bypublictransportpicture", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:bypublictransportvideo")
    public HippoHtml getBypublictransportvideo() {
        return getHippoHtml("website:bypublictransportvideo");
    }

    public String getSectionType() {
        return "directiontositebypublictransport";
    }

}
