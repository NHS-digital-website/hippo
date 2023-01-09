package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:roadmapitem")
@Node(jcrType = "website:roadmapitem")
public class RoadmapItem extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:categorylink")
    public String getCategoryLink() {
        return getSingleProperty("website:categorylink");
    }

    @HippoEssentialsGenerated(internalName = "website:impactedservices")
    public List<HippoBean> getImpactedServices() {
        return getLinkedBeans("website:impactedservices", HippoBean.class);
    }

    @HippoEssentialsGenerated(internalName = "website:effectivedate")
    public EffectiveDate getEffectiveDate() {
        return getBean("website:effectivedate", EffectiveDate.class);
    }

    @HippoEssentialsGenerated(internalName = "website:standards")
    public List<HippoBean> getStandards() {
        return getChildBeansByName("website:standards");
    }

    @HippoEssentialsGenerated(internalName = "website:sections")
    public List<HippoBean> getSections() {
        return getChildBeansByName("website:sections");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<?> getBlocks() {
        return getChildBeansByName("website:items");
    }

    @HippoEssentialsGenerated(internalName = "website:roadmapitemstatuses")
    public RoadmapItemStatus getRoadmapItemStatuses() {
        return getBean("website:roadmapitemstatuses", RoadmapItemStatus.class);
    }

    public String getId() {
        return this.getCanonicalUUID();
    }
}
