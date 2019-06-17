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
        return getProperty("website:buildinglocation");
    }

    @HippoEssentialsGenerated(internalName = "website:buildingname")
    public String getBuildingname() {
        return getProperty("website:buildingname");
    }

    @HippoEssentialsGenerated(internalName = "website:street")
    public String getStreet() {
        return getProperty("website:street");
    }

    @HippoEssentialsGenerated(internalName = "website:area")
    public String getArea() {
        return getProperty("website:area");
    }

    @HippoEssentialsGenerated(internalName = "website:city")
    public String getCity() {
        return getProperty("website:city");
    }

    @HippoEssentialsGenerated(internalName = "website:county")
    public String getCounty() {
        return getProperty("website:county");
    }

    @HippoEssentialsGenerated(internalName = "website:country")
    public String getCountry() {
        return getProperty("website:country");
    }

    @HippoEssentialsGenerated(internalName = "website:postalCode")
    public String getPostalCode() {
        return getProperty("website:postalCode");
    }

    @HippoEssentialsGenerated(internalName = "website:geocoordinates")
    public HippoHtml getGeocoordinates() {
        return getHippoHtml("website:geocoordinates");
    }

    public String getSectionType() {
        return "address";
    }

}
