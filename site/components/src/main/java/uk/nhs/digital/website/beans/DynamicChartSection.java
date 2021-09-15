package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:dynamicchartsection")
@Node(jcrType = "website:dynamicchartsection")
public class DynamicChartSection extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:type")
    public String getType() {
        return getSingleProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:url")
    public String getUrl() {
        return getSingleProperty("website:url");
    }

    @HippoEssentialsGenerated(internalName = "website:yTitle")
    public String getYTitle() {
        return getSingleProperty("website:yTitle");
    }

    @HippoEssentialsGenerated(internalName = "website:xTitle")
    public String getXTitle() {
        return getSingleProperty("website:xTitle");
    }

    @HippoEssentialsGenerated(internalName = "website:htmlCode")
    public String getHtmlCode() {
        return getSingleProperty("website:htmlCode");
    }

    @HippoEssentialsGenerated(internalName = "website:highchartseries")
    public List<HighchartSeries> getSeriesItem() {
        return getChildBeansByName("website:highchartseries",
            HighchartSeries.class);
    }

    /**
     * This is needed for the HTML element that contains the chart to have a unique ID
     */
    public String getUniqueId() {
        return String.valueOf(getPath().hashCode());
    }

    public String getSectionType() {
        return "dynamicChart";
    }
}
