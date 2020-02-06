package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:contactdetailextended")
@Node(jcrType = "website:contactdetailextended")
public class ContactDetailExtended extends ContactDetail {

    @HippoEssentialsGenerated(internalName = "website:purpose", allowModifications = false)
    public String getPurpose() {
        return getProperty("website:purpose");
    }

    @HippoEssentialsGenerated(internalName = "website:description", allowModifications = false)
    public String getDescription() {
        return getProperty("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:twitterhandle", allowModifications = false)
    public String getTwitterHandle() {
        return getProperty("website:twitterhandle");
    }

    @HippoEssentialsGenerated(internalName = "website:webchatdescription", allowModifications = false)
    public String getWebchatDescription() {
        return getProperty("website:webchatdescription");
    }

    @HippoEssentialsGenerated(internalName = "website:webchatlink", allowModifications = false)
    public String getWebchatLink() {
        return getProperty("website:webchatlink");
    }

    @HippoEssentialsGenerated(internalName = "website:webformdescription", allowModifications = false)
    public String getWebformDescription() {
        return getProperty("website:webformdescription");
    }

    @HippoEssentialsGenerated(internalName = "website:webformlink", allowModifications = false)
    public String getWebformLink() {
        return getProperty("website:webformlink");
    }
}
