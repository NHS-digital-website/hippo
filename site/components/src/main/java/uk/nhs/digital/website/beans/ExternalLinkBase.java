package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:externallinkbase")
@Node(jcrType = "website:externallinkbase")
public class ExternalLinkBase extends HippoCompound {

    // used to differentiate between different types of content blocks
    public String getLinkType() {
        return "external";
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public String getLink() {
        return getSingleProperty("website:link");
    }

}
