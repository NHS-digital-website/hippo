package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "website:qualification")
@Node(jcrType = "website:qualification")
public class Qualification extends HippoCompound {

    public String getSectionType() {
        return "qualification";
    }

    @HippoEssentialsGenerated(internalName = "website:qualname")
    public String getQualname() {
        return getSingleProperty("website:qualname");
    }

    @HippoEssentialsGenerated(internalName = "website:quallink")
    public Externallink getQuallink() {
        return getBean("website:quallink", Externallink.class);
    }

    @HippoEssentialsGenerated(internalName = "website:qualawardingbody")
    public String getQualawardingbody() {
        return getSingleProperty("website:qualawardingbody");
    }

    @HippoEssentialsGenerated(internalName = "website:qualawardingbodylink")
    public Externallink getQualawardingbodylink() {
        return getBean("website:qualawardingbodylink", Externallink.class);
    }

    @HippoEssentialsGenerated(internalName = "website:qualificationlogo")
    public CorporateWebsiteImageset getQualificationlogo() {
        return getLinkedBean("website:qualificationlogo", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:dateattained")
    public Calendar getDateattained() {
        return getSingleProperty("website:dateattained");
    }

}
