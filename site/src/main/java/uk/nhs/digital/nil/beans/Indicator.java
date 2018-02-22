package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:indicator")
@Node(jcrType = "nationalindicatorlibrary:indicator")
public class Indicator extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:topbar")
    public Topbar getTopbar() {
        return getBean("nationalindicatorlibrary:topbar", Topbar.class);
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:details")
    public Details getDetails() {
        return getBean("nationalindicatorlibrary:details", Details.class);
    }
}
