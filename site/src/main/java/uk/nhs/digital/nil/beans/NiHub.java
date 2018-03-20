package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.BaseDocument;

import java.util.List;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:nihub")
@Node(jcrType = "nationalindicatorlibrary:nihub")
public class NiHub extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:title")
    public String getTitle() {
        return getProperty("nationalindicatorlibrary:title");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:summary")
    public String getSummary() {
        return getProperty("nationalindicatorlibrary:summary");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:popularTopicLinks")
    public List<ActionLink> getPopularTopicLinks() {
        return getChildBeansByName(
                "nationalindicatorlibrary:popularTopicLinks", ActionLink.class);
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:advice")
    public NiHubLink getAdvice() {
        return getBean("nationalindicatorlibrary:advice", NiHubLink.class);
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:addYourIndicator")
    public NiHubLink getAddYourIndicator() {
        return getBean("nationalindicatorlibrary:addYourIndicator",
                NiHubLink.class);
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:applyForAssurance")
    public NiHubLink getApplyForAssurance() {
        return getBean("nationalindicatorlibrary:applyForAssurance",
                NiHubLink.class);
    }
}
