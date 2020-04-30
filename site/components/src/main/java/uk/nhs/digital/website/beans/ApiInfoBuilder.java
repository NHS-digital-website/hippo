package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:apiinfobuilder")
@Node(jcrType = "website:apiinfobuilder")
public class ApiInfoBuilder extends HippoCompound {

    public String getSectionType() {
        return "apiinfobuilder";
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:content")
    public List<?> getContent() {
        return getChildBeansByName("website:content");
    }

}
