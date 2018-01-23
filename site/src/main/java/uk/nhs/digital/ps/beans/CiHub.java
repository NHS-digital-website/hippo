package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;

import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:cihub")
@Node(jcrType = "publicationsystem:cihub")
public class CiHub extends BaseDocument {

    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:summary")
    public StructuredText getSummary() {
        return new StructuredText(getProperty("publicationsystem:summary", ""));
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:cihublink")
    public List<CiHubLink> getCiHubLink() {
        return getChildBeansByName("publicationsystem:cihublink",
                CiHubLink.class);
    }
}
