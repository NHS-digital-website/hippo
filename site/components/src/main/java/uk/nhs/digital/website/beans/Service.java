package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
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
    public HippoGalleryImageSet getPageIcon() {
        return getLinkedBean("website:pageicon", HippoGalleryImageSet.class);
    }

    public String searchType() {
        return "service";
    }
}
