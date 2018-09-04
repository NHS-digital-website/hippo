package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;

import java.util.*;

@HippoEssentialsGenerated(internalName = "website:general")
@Node(jcrType = "website:general")
public class General extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:seosummary")
    public String getSeosummary() {
        return getProperty("website:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getShortsummary() {
        return getProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:summary")
    public String getSummary() {
        return getProperty("website:summary");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:component")
    public HippoHtml getComponent() {
        return getHippoHtml("website:component");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<?> getBlocks() {
        return getChildBeansByName("website:items");
    }

    @HippoEssentialsGenerated(internalName = "website:type")
    public String getType() {
        return getProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:gossid")
    public Long getGossid() {
        return getProperty("website:gossid");
    }
}
