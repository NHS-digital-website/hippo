package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:apimaster")
@Node(jcrType = "website:apimaster")
public class ApiMaster extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:apiname")
    public String getApiname() {
        return getProperty("website:apiname");
    }

    @HippoEssentialsGenerated(internalName = "website:apiservice")
    public List<HippoBean> getApiservice() {
        return getChildBeansByName("website:apiservice");
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:apitopics")
    public String[] getApitopics() {
        return getProperty("hippotaxonomy:apitopics");
    }

    @HippoEssentialsGenerated(internalName = "website:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("website:introduction");
    }

    @HippoEssentialsGenerated(internalName = "website:aboutapis")
    public List<HippoBean> getAboutapis() {
        return getChildBeansByName("website:aboutapis");
    }

    @HippoEssentialsGenerated(internalName = "website:releaseinfos")
    public List<HippoBean> getReleaseinfos() {
        return getChildBeansByName("website:releaseinfos");
    }

    @HippoEssentialsGenerated(internalName = "website:apiinfobuilders")
    public List<HippoBean> getApiinfobuilders() {
        return getChildBeansByName("website:apiinfobuilders");
    }

    @HippoEssentialsGenerated(internalName = "website:apiendpointgroups")
    public List<HippoBean> getApiendpointgroups() {
        return getChildBeansByName("website:apiendpointgroups");
    }
}
