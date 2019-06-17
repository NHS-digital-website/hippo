package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:publictransportstation")
@Node(jcrType = "website:publictransportstation")
public class PublicTransportStation extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:type")
    public List<HippoBean> getType() {
        return getChildBeansByName("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:distance")
    public String getDistance() {
        return getProperty("website:distance");
    }

    @HippoEssentialsGenerated(internalName = "website:walkingtime")
    public String getWalkingtime() {
        return getProperty("website:walkingtime");
    }

    @HippoEssentialsGenerated(internalName = "website:drivingtime")
    public String getDrivingtime() {
        return getProperty("website:drivingtime");
    }

    @HippoEssentialsGenerated(internalName = "website:geocoordinates")
    public HippoHtml getGeocoordinates() {
        return getHippoHtml("website:geocoordinates");
    }

    public String getSectionType() {
        return "publictransportstation";
    }

}
