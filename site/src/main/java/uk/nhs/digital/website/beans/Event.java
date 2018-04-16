package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;
import uk.nhs.digital.ps.beans.*;

import java.util.*;

@HippoEssentialsGenerated(internalName = "website:event")
@Node(jcrType = "website:event")
public class Event extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:booking")
    public String getBooking() {
        return getProperty("website:booking");
    }

    @HippoEssentialsGenerated(internalName = "website:display")
    public Boolean getDisplay() {
        return getProperty("website:display");
    }

    @HippoEssentialsGenerated(internalName = "website:location")
    public String getLocation() {
        return getProperty("website:location");
    }

    @HippoEssentialsGenerated(internalName = "website:maplocation")
    public String getMaplocation() {
        return getProperty("website:maplocation");
    }

    @HippoEssentialsGenerated(internalName = "website:seosummary")
    public String getSeosummary() {
        return getProperty("website:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getShortsummary() {
        return getProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:type")
    public String[] getType() {
        return getProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:body")
    public HippoHtml getBody() {
        return getHippoHtml("website:body");
    }

    @HippoEssentialsGenerated(internalName = "website:relatedDocuments")
    public List<HippoBean> getRelatedDocuments() {
        return getLinkedBeans("website:relatedDocuments", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:summaryimage")
    public HippoGalleryImageSet getSummaryimage() {
        return getLinkedBean("website:summaryimage", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:attachments")
    public List<ExtAttachment> getExtAttachments() {
        return getChildBeansByName("website:attachments", ExtAttachment.class);
    }

    @HippoEssentialsGenerated(internalName = "website:events")
    public List<Interval> getEvents() {
        return getChildBeansByName("website:events", Interval.class);
    }
}
