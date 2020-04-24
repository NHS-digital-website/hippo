package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:highchartseries")
@Node(jcrType = "website:highchartseries")
public class HighchartSeries extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:type")
    public String getType() {
        return getProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getProperty("website:name");
    }
}
