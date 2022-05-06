package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:contactdetailextended")
@Node(jcrType = "website:contactdetailextended")
public class ContactDetailExtended extends ContactDetail {

    @HippoEssentialsGenerated(internalName = "website:purpose", allowModifications = false)
    public String getPurpose() {
        return getSingleProperty("website:purpose");
    }

    @HippoEssentialsGenerated(internalName = "website:description", allowModifications = false)
    public String getDescription() {
        return getSingleProperty("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:twitterhandle", allowModifications = false)
    public String getTwitterHandle() {
        return getSingleProperty("website:twitterhandle");
    }

    @HippoEssentialsGenerated(internalName = "website:webchatdescription", allowModifications = false)
    public String getWebchatDescription() {
        return getSingleProperty("website:webchatdescription");
    }

    @HippoEssentialsGenerated(internalName = "website:webchatlink", allowModifications = false)
    public String getWebchatLink() {
        return getSingleProperty("website:webchatlink");
    }

    @HippoEssentialsGenerated(internalName = "website:webformdescription", allowModifications = false)
    public String getWebformDescription() {
        return getSingleProperty("website:webformdescription");
    }

    @HippoEssentialsGenerated(internalName = "website:webformlink", allowModifications = false)
    public String getWebformLink() {
        return getSingleProperty("website:webformlink");
    }
}
