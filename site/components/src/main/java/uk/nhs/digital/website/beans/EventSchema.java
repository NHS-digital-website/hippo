package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "website:EventSchema")
@Node(jcrType = "website:EventSchema")
public class EventSchema extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:eventStatusType")
    public String getEventStatusType() {
        return getSingleProperty("website:eventStatusType");
    }

    @HippoEssentialsGenerated(internalName = "website:scheduleStart")
    public Calendar getScheduleStart() {
        return getSingleProperty("website:scheduleStart");
    }

    @HippoEssentialsGenerated(internalName = "website:scheduleEnd")
    public Calendar getScheduleEnd() {
        return getSingleProperty("website:scheduleEnd");
    }

    @HippoEssentialsGenerated(internalName = "website:scheduleTimezone")
    public String getScheduleTimezone() {
        return getSingleProperty("website:scheduleTimezone");
    }

    @HippoEssentialsGenerated(internalName = "website:addressLocality")
    public String getAddressLocality() {
        return getSingleProperty("website:addressLocality");
    }

    @HippoEssentialsGenerated(internalName = "website:locationType")
    public String getLocationType() {
        return getSingleProperty("website:locationType");
    }

    @HippoEssentialsGenerated(internalName = "website:locationName")
    public String getLocationName() {
        return getSingleProperty("website:locationName");
    }

    @HippoEssentialsGenerated(internalName = "website:locationDescription")
    public String getLocationDescription() {
        return getSingleProperty("website:locationDescription");
    }

    @HippoEssentialsGenerated(internalName = "website:locationURL")
    public String getLocationURL() {
        return getSingleProperty("website:locationURL");
    }

    @HippoEssentialsGenerated(internalName = "website:addressRegion")
    public String getAddressRegion() {
        return getSingleProperty("website:addressRegion");
    }

    @HippoEssentialsGenerated(internalName = "website:postalCode")
    public String getPostalCode() {
        return getSingleProperty("website:postalCode");
    }

    @HippoEssentialsGenerated(internalName = "website:addressStreet")
    public String getAddressStreet() {
        return getSingleProperty("website:addressStreet");
    }
}
