package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:signup")
@Node(jcrType = "website:signup")
public class Signup extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:summary")
    public HippoHtml getSummary() {
        return getHippoHtml("website:summary");
    }

    @HippoEssentialsGenerated(internalName = "website:shortsummary")
    public String getShortsummary() {
        return getSingleProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:seosummary")
    public HippoHtml getSeosummary() {
        return getHippoHtml("website:seosummary");
    }

    @HippoEssentialsGenerated(internalName = "website:owner")
    public List<HippoBean> getOwner() {
        return getChildBeansByName("website:owner");
    }

    @HippoEssentialsGenerated(internalName = "website:introductionsections")
    public List<HippoBean> getIntroductionsections() {
        return getChildBeansByName("website:introductionsections");
    }

    @HippoEssentialsGenerated(internalName = "website:footersections")
    public List<HippoBean> getFootersections() {
        return getChildBeansByName("website:footersections");
    }

    @HippoEssentialsGenerated(internalName = "website:classifiable", allowModifications = false)
    public String[] getClassifiable() {
        return getMultipleProperty("website:classifiable");
    }

    @HippoEssentialsGenerated(internalName = "website:bannercontrols")
    public BannerControl getBannercontrols() {
        return getBean("intranet:bannercontrols", BannerControl.class);
    }

}
