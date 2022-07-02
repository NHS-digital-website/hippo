package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:eventSchema")
@Node(jcrType = "website:eventSchema")
public class EventSchema extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:eventstatustype")
    public String getEventstatustype() {
        return getSingleProperty("website:eventstatustype");
    }

    @HippoEssentialsGenerated(internalName = "website:addresslocality")
    public String getAddresslocality() {
        return getSingleProperty("website:addresslocality");
    }

    @HippoEssentialsGenerated(internalName = "website:addressregion")
    public String getAddressregion() {
        return getSingleProperty("website:addressregion");
    }
}
