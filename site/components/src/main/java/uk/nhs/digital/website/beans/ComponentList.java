package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.*;

import java.util.*;

@HippoEssentialsGenerated(internalName = "website:componentlist")
@Node(jcrType = "website:componentlist")
public class ComponentList extends CommonFieldsBean {
    @HippoEssentialsGenerated(internalName = "website:anchor")
    public Boolean getAnchor() {
        return getSingleProperty("website:anchor");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<?> getBlocks() {
        return getChildBeansByName("website:items");
    }

    @HippoEssentialsGenerated(internalName = "website:gossid")
    public Long getGossid() {
        return getSingleProperty("website:gossid");
    }

    @HippoEssentialsGenerated(internalName = "website:body")
    public HippoHtml getBody() {
        return getHippoHtml("website:body");
    }

    @HippoEssentialsGenerated(internalName = "website:entriesFooterContentTitle")
    public String getEntriesFooterContentTitle() {
        return getSingleProperty("website:entriesFooterContentTitle");
    }

    @HippoEssentialsGenerated(internalName = "website:entriesFooterContentBody")
    public HippoHtml getEntriesFooterContentBody() {
        return getHippoHtml("website:entriesFooterContentBody");
    }
}
