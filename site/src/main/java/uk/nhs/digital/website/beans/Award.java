package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:award")
@Node(jcrType = "website:award")
public class Award extends HippoCompound {

    public String getSectionType() {
        return "award";
    }

    @HippoEssentialsGenerated(internalName = "website:awardname")
    public String getAwardname() {
        return getProperty("website:awardname");
    }

    @HippoEssentialsGenerated(internalName = "website:awardlink")
    public List<?> getAwardlink() {
        return getChildBeansByName("website:awardlink");
    }

    @HippoEssentialsGenerated(internalName = "website:awardingbody")
    public String getAwardingbody() {
        return getProperty("website:awardingbody");
    }

    @HippoEssentialsGenerated(internalName = "website:awardingbodylink")
    public List<?> getAwardingbodylink() {
        return getChildBeansByName("website:awardingbodylink");
    }

    @HippoEssentialsGenerated(internalName = "website:awardpicture")
    public HippoGalleryImageSet getAwardpicture() {
        return getLinkedBean("website:awardpicture", HippoGalleryImageSet.class);
    }

}
