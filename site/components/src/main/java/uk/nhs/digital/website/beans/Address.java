package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:address")
@Node(jcrType = "website:address")
public class Address extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:buildinglocation")
    public String getBuildinglocation() {
        return getSingleProperty("website:buildinglocation");
    }

    @HippoEssentialsGenerated(internalName = "website:buildingname")
    public String getBuildingname() {
        return getSingleProperty("website:buildingname");
    }

    @HippoEssentialsGenerated(internalName = "website:street")
    public String getStreet() {
        return getSingleProperty("website:street");
    }

    @HippoEssentialsGenerated(internalName = "website:area")
    public String getArea() {
        return getSingleProperty("website:area");
    }

    @HippoEssentialsGenerated(internalName = "website:city")
    public String getCity() {
        return getSingleProperty("website:city");
    }

    @HippoEssentialsGenerated(internalName = "website:county")
    public String getCounty() {
        return getSingleProperty("website:county");
    }

    @HippoEssentialsGenerated(internalName = "website:country")
    public String getCountry() {
        return getSingleProperty("website:country");
    }

    @HippoEssentialsGenerated(internalName = "website:postalCode")
    public String getPostalCode() {
        return getSingleProperty("website:postalCode");
    }

    @HippoEssentialsGenerated(internalName = "website:maplink")
    public String getMapLink() {
        return getSingleProperty("website:maplink");
    }

    @HippoEssentialsGenerated(internalName = "website:geocoordinates")
    public HippoHtml getGeocoordinates() {
        return getHippoHtml("website:geocoordinates");
    }

    public String getSectionType() {
        return "address";
    }

}
