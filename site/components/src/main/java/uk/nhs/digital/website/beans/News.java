package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;

import java.util.*;

@HippoEssentialsGenerated(internalName = "website:news")
@Node(jcrType = "website:news")
public class News extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "website:creditbanner")
    public String getCreditBanner() {
        return getProperty("website:creditbanner");
    }

    @HippoEssentialsGenerated(internalName = "website:publisheddatetime")
    public Calendar getPublisheddatetime() {
        return getProperty("website:publisheddatetime");
    }

    @HippoEssentialsGenerated(internalName = "website:seosummary")
    public String getSeosummary() {
        return getProperty("website:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getShortsummary() {
        return getProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:theme")
    public String getTheme() {
        return getProperty("website:theme");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:type")
    public String getType() {
        return getProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:body")
    public HippoHtml getBody() {
        return getHippoHtml("website:body");
    }

    @HippoEssentialsGenerated(internalName = "website:relateddocuments")
    public List<HippoBean> getRelateddocuments() {
        return getLinkedBeans("website:relateddocuments", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:display")
    public Boolean getDisplay() {
        return getProperty("website:display");
    }
}
