package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "website:eventSchema")
@Node(jcrType = "website:eventSchema")
public class EventSchema extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:eventtype")
    public String getEventtype() {
        return getSingleProperty("website:eventtype");
    }

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

    @HippoEssentialsGenerated(internalName = "website:apostalcode")
    public String getPostalcode() {
        return getSingleProperty("website:postalcode");
    }

    @HippoEssentialsGenerated(internalName = "website:streetaddress")
    public String getStreetaddress() {
        return getSingleProperty("website:streetaddress");
    }

    @HippoEssentialsGenerated(internalName = "website:locationname")
    public String getLocationname() {
        return getSingleProperty("website:locationname");
    }

    @HippoEssentialsGenerated(internalName = "website:virtuallocation")
    public String getVirtuallocation() {
        return getSingleProperty("website:virtuallocation");
    }

    @HippoEssentialsGenerated(internalName = "website:price")
    public String getPrice() {
        return getSingleProperty("website:price");
    }

    @HippoEssentialsGenerated(internalName = "website:priceurl")
    public String getPriceurl() {
        return getSingleProperty("website:priceurl");
    }

    @HippoEssentialsGenerated(internalName = "website:startdate")
    public Calendar getStartdate() {
        return getSingleProperty("website:startdate");
    }

    @HippoEssentialsGenerated(internalName = "website:enddate")
    public Calendar getEnddate() {
        return getSingleProperty("website:enddate");
    }

    @HippoEssentialsGenerated(internalName = "website:duration")
    public String getDuration() {
        return getSingleProperty("website:duration");
    }

    @HippoEssentialsGenerated(internalName = "website:eventname")
    public String getEventname() {
        return getSingleProperty("website:eventname");
    }

    @HippoEssentialsGenerated(internalName = "website:eventdescription")
    public String getEventdescription() {
        return getSingleProperty("website:eventdescription");
    }
}
