package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.*;
import org.hippoecm.hst.content.beans.standard.*;
import org.onehippo.cms7.essentials.dashboard.annotations.*;

import java.util.*;

@HippoEssentialsGenerated(internalName = "website:hub")
@Node(jcrType = "website:hub")
public class Hub extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:listtitle")
    public String getListtitle() {
        return getProperty("website:listtitle");
    }

    @HippoEssentialsGenerated(internalName = "website:component")
    public HippoHtml getComponent() {
        return getHippoHtml("website:component");
    }

    @HippoEssentialsGenerated(internalName = "website:componentlist")
    public List<HippoBean> getComponentlist() {
        return getLinkedBeans("website:componentlist", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:body")
    public HippoHtml getBody() {
        return getHippoHtml("website:body");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<?> getBlocks() {
        return getChildBeansByName("website:items");
    }

    @HippoEssentialsGenerated(internalName = "website:gossid")
    public Long getGossid() {
        return getProperty("website:gossid");
    }
}
