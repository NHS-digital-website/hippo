package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:statisticsFeedItem")
@Node(jcrType = "website:statisticsFeedItem")
public class StatisticsFeedItem extends StatisticCommonItem {

    @HippoEssentialsGenerated(internalName = "website:urlOfNumber")
    public String getUrlOfNumber() {
        return getSingleProperty("website:urlOfNumber");
    }

    public String getStatisticType() {
        return "feedStatistic";
    }
}
