package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:roadmap")
@Node(jcrType = "website:roadmap")
public class Roadmap extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:linkedservices")
    public List<HippoBean> getLinkedServices() {
        return getLinkedBeans("website:linkedservices", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:granularity")
    public String getGranularity() {
        return getProperty("website:granularity");
    }

    @HippoEssentialsGenerated(internalName = "website:categories")
    public List<HippoBean> getCategories() {
        return getChildBeansByName("website:categories");
    }

    @HippoEssentialsGenerated(internalName = "website:item")
    public List<HippoBean> getItem() {
        return getLinkedBeans("website:item", HippoBean.class);
    }

}
