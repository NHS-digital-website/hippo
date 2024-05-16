package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:externallink")
@Node(jcrType = "website:externallink")
public class Externallink extends ExternalLinkBase {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getShortsummary() {
        return getSingleProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "jcr:uuid")
    public String getId() {
        return getSingleProperty("jcr:uuid");
    }
}
