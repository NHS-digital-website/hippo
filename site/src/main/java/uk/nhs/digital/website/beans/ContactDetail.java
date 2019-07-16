package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:contactdetail")
@Node(jcrType = "website:contactdetail")
public class ContactDetail extends HippoCompound {

    public String getSectionType() {
        return "contactdetail";
    }

    @HippoEssentialsGenerated(internalName = "website:emailaddress")
    public String getEmailaddress() {
        return getProperty("website:emailaddress");
    }

    @HippoEssentialsGenerated(internalName = "website:phonenumber")
    public String getPhonenumber() {
        return getProperty("website:phonenumber");
    }
}
