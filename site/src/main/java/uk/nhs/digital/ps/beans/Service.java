package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;

import java.util.*;

@HippoEssentialsGenerated(internalName = "publicationsystem:service")
@Node(jcrType = "publicationsystem:service")
public class Service extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:summary")
    public String getSummary() {
        return getProperty("publicationsystem:summary");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:shortsummary")
    public String getShortsummary() {
        return getProperty("publicationsystem:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:seosummary")
    public String getSeosummary() {
        return getProperty("publicationsystem:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:toptasks")
    public List<HippoHtml> getToptasks() {
        return getChildBeansByName("publicationsystem:toptasks",
                HippoHtml.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("publicationsystem:introduction");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:sections")
    public List<Section> getSections() {
        return getChildBeansByName("publicationsystem:sections", Section.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:contactdetails")
    public HippoHtml getContactdetails() {
        return getHippoHtml("publicationsystem:contactdetails");
    }
}
