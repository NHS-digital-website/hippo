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
        return getSingleProperty("website:granularity");
    }

    @HippoEssentialsGenerated(internalName = "website:item")
    public List<RoadmapItem> getItem() {
        return getLinkedBeans("website:item", RoadmapItem.class);
    }

    @HippoEssentialsGenerated(internalName = "website:roadmapCategories")
    public List<Categories> getRoadmapCategories() {
        return getChildBeansByName("website:roadmapCategories", Categories.class);
    }
}
