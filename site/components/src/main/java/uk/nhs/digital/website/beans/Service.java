package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:service")
@Node(jcrType = "website:service")
public class Service extends CommonFieldsBean {

    public List<PriorityAction> getPriorityActions() {
        return getChildBeansByName("website:priorityaction", PriorityAction.class);
    }

    @HippoEssentialsGenerated(internalName = "website:toptasks")
    public List<HippoHtml> getToptasks() {
        return getChildBeansByName("website:toptasks", HippoHtml.class);
    }

    @HippoEssentialsGenerated(internalName = "website:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("website:introduction");
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:contactdetails")
    public HippoHtml getContactdetails() {
        return getHippoHtml("website:contactdetails");
    }

    @HippoEssentialsGenerated(internalName = "website:component")
    public HippoHtml getComponent() {
        return getHippoHtml("website:component");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<?> getBlocks() {
        return getChildBeansByName("website:items");
    }

    @HippoEssentialsGenerated(internalName = "website:gossid")
    public Long getGossid() {
        return getSingleProperty("website:gossid");
    }

    @HippoEssentialsGenerated(internalName = "website:friendlyurls")
    public Friendlyurls getFriendlyurls() {
        return getBean("website:friendlyurls", Friendlyurls.class);
    }

    @HippoEssentialsGenerated(internalName = "website:htmlCode")
    public String getHtmlCode() {
        return getSingleProperty("website:htmlCode");
    }

    @HippoEssentialsGenerated(internalName = "website:rawMetadata")
    public String[] getRawMetadata() {
        return getMultipleProperty("website:rawMetadata");
    }

    @HippoEssentialsGenerated(internalName = "website:pageicon")
    public CorporateWebsiteImageset getPageIcon() {
        return getLinkedBean("website:pageicon", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:navigationcontroller")
    public String getNavigationController() {
        return getSingleProperty("website:navigationcontroller", "withNav");
    }

    @HippoEssentialsGenerated(internalName = "website:image")
    public CorporateWebsiteImageset getImage() {
        return getLinkedBean("website:image", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:noindexcontrol")
    public boolean getNoIndexControl() {
        return getSingleProperty("website:noindexcontrol");
    }

    public String getEarlyAccessKey() {
        return getSingleProperty("website:earlyaccesskey");
    }
}
