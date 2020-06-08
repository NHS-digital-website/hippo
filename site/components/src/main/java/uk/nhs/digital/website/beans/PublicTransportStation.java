package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;



@HippoEssentialsGenerated(internalName = "website:publictransportstation")
@Node(jcrType = "website:publictransportstation")
public class PublicTransportStation extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:bypublictransportpicture")
    public CorporateWebsiteImageset getByPublicTransportPicture() {
        return getLinkedBean("website:bypublictransportpicture", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:bypublictransportpicturealttext")
    public String getByPublicTransportPictureAltText() {
        return getSingleProperty("website:bypublictransportpicturealttext");
    }

    @HippoEssentialsGenerated(internalName = "website:bypublictransportvideo")
    public HippoHtml getByPublicTransportVideo() {
        return getHippoHtml("website:bypublictransportvideo");
    }

    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getSingleProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:distance")
    public String getDistance() {
        return getSingleProperty("website:distance");
    }

    @HippoEssentialsGenerated(internalName = "website:walkingtime")
    public String getWalkingtime() {
        return getSingleProperty("website:walkingtime");
    }

    @HippoEssentialsGenerated(internalName = "website:drivingtime")
    public String getDrivingtime() {
        return getSingleProperty("website:drivingtime");
    }

    @HippoEssentialsGenerated(internalName = "website:geocoordinates")
    public HippoHtml getGeocoordinates() {
        return getHippoHtml("website:geocoordinates");
    }

    @HippoEssentialsGenerated(internalName = "website:gettolocationfromhere")
    public HippoHtml getGetToLocationFromHere() {
        return getHippoHtml("website:gettolocationfromhere");
    }

    public String getSectionType() {
        return "publictransportstation";
    }

}
