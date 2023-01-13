package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoResource;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "publicationsystem:Visualisation")
@Node(jcrType = "publicationsystem:Visualisation")
public class VisualisationSection extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "publicationsystem:dataFile")
    public HippoResource getDataFile() {
        return getChildBeansByName("publicationsystem:dataFile", HippoResource.class).get(0);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getSingleProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:type")
    public String getType() {
        return getSingleProperty("publicationsystem:type");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:colour")
    public String getColour() {
        return getSingleProperty("publicationsystem:colour");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:chartConfig")
    public String getChartConfig() {
        return getSingleProperty("publicationsystem:chartConfig");
    }

    public String getUniqueId() {
        return String.valueOf(getPath().hashCode());
    }

    public String getSectionType() {
        return "visualisation";
    }
}
