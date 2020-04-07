package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:statisticsItem")
@Node(jcrType = "website:statisticsItem")
public class StatisticsItem extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:prefix")
    public String getPrefix() {
        return getProperty("website:prefix");
    }

    @HippoEssentialsGenerated(internalName = "website:number")
    public String getNumber() {
        return getProperty("website:number");
    }

    @HippoEssentialsGenerated(internalName = "website:suffix")
    public String getSuffix() {
        return getProperty("website:suffix");
    }

    @HippoEssentialsGenerated(internalName = "website:trend")
    public String getTrend() {
        return getProperty("website:trend");
    }

    @HippoEssentialsGenerated(internalName = "website:headlineDescription")
    public HippoHtml getHeadlineDescription() {
        return getHippoHtml("website:headlineDescription");
    }

    @HippoEssentialsGenerated(internalName = "website:furtherQualifyingInformation")
    public HippoHtml getFurtherQualifyingInformation() {
        return getHippoHtml("website:furtherQualifyingInformation");
    }
}
