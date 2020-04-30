package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:onsitecarparking")
@Node(jcrType = "website:onsitecarparking")
public class OnSiteCarParking extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:visitorparkingavailable")
    public Boolean getVisitorparkingavailable() {
        return getSingleProperty("website:visitorparkingavailable");
    }

    @HippoEssentialsGenerated(internalName = "website:numberofspaces")
    public Long getNumberofspaces() {
        return getSingleProperty("website:numberofspaces");
    }

    @HippoEssentialsGenerated(internalName = "website:detailsofparking")
    public String getDetailsofparking() {
        return getSingleProperty("website:detailsofparking");
    }

    @HippoEssentialsGenerated(internalName = "website:parkingnearby")
    public Boolean getParkingnearby() {
        return getSingleProperty("website:parkingnearby");
    }

    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getSingleProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:address")
    public HippoHtml getAddress() {
        return getHippoHtml("website:address");
    }

    @HippoEssentialsGenerated(internalName = "website:distancefromsite")
    public String getDistancefromsite() {
        return getSingleProperty("website:distancefromsite");
    }

    @HippoEssentialsGenerated(internalName = "website:websitelink")
    public Externallink getWebsiteLink() {
        List<Externallink> externalLinkList = getChildBeansByName("website:websitelink", Externallink.class);
        if (externalLinkList != null && !externalLinkList.isEmpty()) {
            return externalLinkList.get(0);
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:costs")
    public String getCosts() {
        return getSingleProperty("website:costs");
    }

    @HippoEssentialsGenerated(internalName = "website:geocoordinates")
    public HippoHtml getGeocoordinates() {
        return getHippoHtml("website:geocoordinates");
    }

    public String getSectionType() {
        return "onsitecarparking";
    }
}
