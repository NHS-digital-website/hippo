package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:award")
@Node(jcrType = "website:award")
public class Award extends HippoCompound {

    public String getSectionType() {
        return "award";
    }

    @HippoEssentialsGenerated(internalName = "website:awardname")
    public String getAwardname() {
        return getSingleProperty("website:awardname");
    }

    @HippoEssentialsGenerated(internalName = "website:awardlink")
    public Externallink getAwardlink() {
        return getBean("website:awardlink", Externallink.class);
    }

    @HippoEssentialsGenerated(internalName = "website:awardingbody")
    public String getAwardingbody() {
        return getSingleProperty("website:awardingbody");
    }

    @HippoEssentialsGenerated(internalName = "website:awardingbodylink")
    public Externallink getAwardingbodylink() {
        return getBean("website:awardingbodylink", Externallink.class);
    }

    @HippoEssentialsGenerated(internalName = "website:awardpicture")
    public CorporateWebsiteImageset getAwardpicture() {
        return getLinkedBean("website:awardpicture", CorporateWebsiteImageset.class);
    }

}
