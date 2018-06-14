package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoResource;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "publicationsystem:mapSection")
@Node(jcrType = "publicationsystem:mapSection")
public class MapSection extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "publicationsystem:dataFile")
    public HippoResource getDataFile() {
        return getChildBeansByName("publicationsystem:dataFile", HippoResource.class).get(0);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:type")
    public String getMapSource() {
        return getProperty("publicationsystem:mapSource");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:chartConfig")
    public String getChartConfig() {
        return getProperty("publicationsystem:chartConfig");
    }

    /**
     * This is needed for the HTML element that contains the map to have a unique ID
     */
    public String getUniqueId() {
        return String.valueOf(getPath().hashCode());
    }

    public String getSectionType() {
        return "map";
    }
}
