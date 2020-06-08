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
        return getSingleProperty("website:booking");
    }

    @HippoEssentialsGenerated(internalName = "website:display")
    public Boolean getDisplay() {
        return getSingleProperty("website:display");
    }

    @HippoEssentialsGenerated(internalName = "website:location")
    public String getLocation() {
        return getSingleProperty("website:location");
    }

    @HippoEssentialsGenerated(internalName = "website:maplocation")
    public String getMaplocation() {
        return getSingleProperty("website:maplocation");
    }

    @HippoEssentialsGenerated(internalName = "website:seosummary")
    public HippoHtml getSeosummary() {
        return getHippoHtml("website:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getShortsummary() {
        return getSingleProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:type")
    public String[] getType() {
        return getMultipleProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:body")
    public HippoHtml getBody() {
        return getHippoHtml("website:body");
    }

    @HippoEssentialsGenerated(internalName = "website:relatedDocuments")
    public List<HippoBean> getRelatedDocuments() {
        return getLinkedBeans("website:relatedDocuments", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:peoplementioned")
    public List<HippoBean> getPeoplementioned() {
        return getLinkedBeans("website:peoplementioned", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:summaryimage")
    public CorporateWebsiteImageset getSummaryimage() {
        return getLinkedBean("website:summaryimage", CorporateWebsiteImageset.class);
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
