package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:localtaxi")
@Node(jcrType = "website:localtaxi")
public class LocalTaxi extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:taxicompanyname")
    public String getTaxicompanyname() {
        return getSingleProperty("website:taxicompanyname");
    }

    @HippoEssentialsGenerated(internalName = "website:taxitelephonenumber")
    public String getTaxitelephonenumber() {
        return getSingleProperty("website:taxitelephonenumber");
    }

    public String getSectionType() {
        return "localtaxi";
    }

}
