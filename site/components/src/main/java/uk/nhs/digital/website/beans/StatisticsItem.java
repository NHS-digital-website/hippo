package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:statisticsItem")
@Node(jcrType = "website:statisticsItem")
public class StatisticsItem extends StatisticCommonItem {

    @HippoEssentialsGenerated(internalName = "website:number")
    public String getNumber() {
        return getSingleProperty("website:number");
    }

    public String getStatisticType() {
        return "staticStatistic";
    }
}
