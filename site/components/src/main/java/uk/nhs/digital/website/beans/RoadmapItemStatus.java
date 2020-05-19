package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;


@HippoEssentialsGenerated(internalName = "website:roadmapitemstatus")
@Node(jcrType = "website:roadmapitemstatus")
public class RoadmapItemStatus extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:status")
    public String getStatus() {
        return getSingleProperty("website:status");
    }

    @HippoEssentialsGenerated(internalName = "website:completeddate")
    public Calendar getCompletedDate() {
        return getSingleProperty("website:completeddate");
    }

    @HippoEssentialsGenerated(internalName = "website:supersededdate")
    public Calendar getSupersededDate() {
        return getSingleProperty("website:supersededdate");
    }

    @HippoEssentialsGenerated(internalName = "website:progress")
    public String getProgress() {
        return getSingleProperty("website:progress");
    }

    public String getSectionType() {
        return "roadmapitemstatus";
    }
}
