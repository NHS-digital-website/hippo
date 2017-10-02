package uk.nhs.digital.ps.beans;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.Node;

@HippoEssentialsGenerated(internalName = "publicationsystem:publication")
@Node(jcrType = "publicationsystem:publication")
public class Publication extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:summary")
    public String getSummary() {
        return getProperty("publicationsystem:summary");
    }
}
