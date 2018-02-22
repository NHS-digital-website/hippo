package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:nihublink")
@Node(jcrType = "nationalindicatorlibrary:nihublink")
public class NiHubLink extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:title")
    public String getTitle() {
        return getProperty("nationalindicatorlibrary:title");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:summary")
    public String getSummary() {
        return getProperty("nationalindicatorlibrary:summary");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:relativeUrl")
    public String getPageLink() {
        return getProperty("nationalindicatorlibrary:relativeUrl");
    }
}
