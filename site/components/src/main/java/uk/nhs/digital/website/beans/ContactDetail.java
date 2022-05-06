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

    @HippoEssentialsGenerated(internalName = "website:name", allowModifications = false)
    public String getName() {
        return getSingleProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:emailaddress", allowModifications = false)
    public String getEmailaddress() {
        return getSingleProperty("website:emailaddress");
    }

    @HippoEssentialsGenerated(internalName = "website:phonenumber", allowModifications = false)
    public String getPhonenumber() {
        return getSingleProperty("website:phonenumber");
    }
}
