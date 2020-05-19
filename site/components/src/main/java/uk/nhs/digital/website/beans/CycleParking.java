package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:cycleparking")
@Node(jcrType = "website:cycleparking")
public class CycleParking extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:available")
    public boolean getAvailable() {
        return getSingleProperty("website:available");
    }

    @HippoEssentialsGenerated(internalName = "website:numberofspaces")
    public Long getNumberofspaces() {
        return getSingleProperty("website:numberofspaces");
    }

    @HippoEssentialsGenerated(internalName = "website:details")
    public String getDetails() {
        return getSingleProperty("website:details");
    }

    public String getSectionType() {
        return "cycleparking";
    }

}
