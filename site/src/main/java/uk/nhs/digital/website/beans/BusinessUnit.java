package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:businessunit")
@Node(jcrType = "website:businessunit")
public class BusinessUnit extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:portfoliocode")
    public String getPortfoliocode() {
        return getProperty("website:portfoliocode");
    }

    @HippoEssentialsGenerated(internalName = "website:vision")
    public HippoHtml getVision() {
        return getHippoHtml("website:vision");
    }

    @HippoEssentialsGenerated(internalName = "website:purposes")
    public List<HippoHtml> getPurposes() {
        return getChildBeansByName("website:purposes", HippoHtml.class);
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:ispartofbusinessunit")
    public HippoBean getIspartofbusinessunit() {
        return getLinkedBean("website:ispartofbusinessunit", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:bannercontrols")
    public BannerControl  getBannercontrols() {
        return getBean("website:bannercontrols", BannerControl.class);
    }

}
