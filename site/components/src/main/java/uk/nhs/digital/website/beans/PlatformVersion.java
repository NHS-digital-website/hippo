package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:platformversion")
@Node(jcrType = "website:platformversion")
public class PlatformVersion extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:versionnumber")
    public String getVersion() {
        return getSingleProperty("website:versionnumber");
    }

    @HippoEssentialsGenerated(internalName = "website:status")
    public String getStatus() {
        return getSingleProperty("website:status");
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public String getLink() {
        return getSingleProperty("website:link");
    }

}
