package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:transportroutes")
@Node(jcrType = "website:transportroutes")
public class TransportRoutes extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:routenumber")
    public String getRoutenumber() {
        return getProperty("website:routenumber");
    }

    @HippoEssentialsGenerated(internalName = "website:routelink")
    public List<?> getRoutelink() {
        return getChildBeansByName("website:routelink");
    }

    public String getSectionType() {
        return "transportroutes";
    }

}
