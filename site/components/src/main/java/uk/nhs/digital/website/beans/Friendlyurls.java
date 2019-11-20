package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:friendlyurls")
@Node(jcrType = "website:friendlyurls")
public class Friendlyurls extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:alternativeurls")
    public String[] getAlternativeurls() {
        return getProperty("website:alternativeurls");
    }

    @HippoEssentialsGenerated(internalName = "website:destination")
    public String getDestination() {
        return getProperty("website:destination");
    }
}
