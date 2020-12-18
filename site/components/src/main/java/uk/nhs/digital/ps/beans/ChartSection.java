package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoResource;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "publicationsystem:chartSection")
@Node(jcrType = "publicationsystem:chartSection")
public class ChartSection extends HippoCompound {

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

    @HippoEssentialsGenerated(internalName = "publicationsystem:yTitle")
    public String getYTitle() {
        return getSingleProperty("publicationsystem:yTitle");
    }

    @HippoEssentialsGenerated(internalName = "website:htmlCode")
    public String getHtmlCode() {
        return getSingleProperty("website:htmlCode");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:chartConfig")
    public String getChartConfig() {
        return getSingleProperty("publicationsystem:chartConfig");
    }

    /**
     * This is needed for the HTML element that contains the chart to have a unique ID
     */
    public String getUniqueId() {
        return String.valueOf(getPath().hashCode());
    }

    public String getSectionType() {
        return "chart";
    }
}
