package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:authnandauths")
@Node(jcrType = "website:authnandauths")
public class AuthnAndAuths extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:content")
    public HippoHtml getContent() {
        return getHippoHtml("website:content");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    public String getSectionType() {
        return "authnandauths";
    }
}
