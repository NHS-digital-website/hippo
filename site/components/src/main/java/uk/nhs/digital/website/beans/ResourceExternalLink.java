package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:resourceexternallink")
@Node(jcrType = "website:resourceexternallink")
public class ResourceExternalLink extends ExternalLinkBase {

    @HippoEssentialsGenerated(internalName = "website:usedexternalservice")
    public String getExternalService() {
        return getProperty("website:usedexternalservice");
    }

}
