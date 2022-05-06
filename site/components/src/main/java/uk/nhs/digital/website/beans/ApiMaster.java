package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.HippoBeanHelper;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:apimaster")
@Node(jcrType = "website:apimaster")
public class ApiMaster extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:apiservice")
    public List<HippoBean> getApiservice() {
        return getLinkedBeans("website:apiservice", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "hippotaxonomy:keys")
    public String[] getKeys() {
        return getMultipleProperty("hippotaxonomy:keys");
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
    public ReleaseInfo getReleaseinfos() {
        return getBean("website:releaseinfos", ReleaseInfo.class);
    }

    @HippoEssentialsGenerated(internalName = "website:apiinfobuilders")
    public List<HippoBean> getApiinfobuilders() {
        return getChildBeansByName("website:apiinfobuilders");
    }

    @HippoEssentialsGenerated(internalName = "website:apiendpointgroups")
    public List<HippoBean> getApiendpointgroups() {
        return getChildBeansByName("website:apiendpointgroups");
    }

    public List<String> getFullTaxonomyList() {
        return HippoBeanHelper.getFullTaxonomyList(this);
    }
}
