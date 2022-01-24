package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:general")
@Node(jcrType = "website:general")
public class General extends CommonFieldsBean {

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
        return getSingleProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:gossid")
    public Long getGossid() {
        return getSingleProperty("website:gossid");
    }

    @HippoEssentialsGenerated(internalName = "website:htmlCode")
    public String getHtmlCode() {
        return getSingleProperty("website:htmlCode");
    }

    @HippoEssentialsGenerated(internalName = "website:metadata")
    public String[] getMetadata() {
        return getMultipleProperty("website:metadata");
    }

    @HippoEssentialsGenerated(internalName = "website:pageicon")
    public CorporateWebsiteImageset getPageIcon() {
        return getLinkedBean("website:pageicon", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:leadimage")
    public CorporateWebsiteImageset getLeadImage() {
        return getLinkedBean("website:leadimage", CorporateWebsiteImageset.class);
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

    @HippoEssentialsGenerated(internalName = "website:propmtuserorg")
    public String[] getPropmtUserOrg() {
        return getMultipleProperty("website:propmtuserorg");
    }

    public String getEarlyAccessKey() {
        return getSingleProperty("website:earlyaccesskey");
    }

}
