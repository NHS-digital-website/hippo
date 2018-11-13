package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;


@HippoEssentialsGenerated(internalName = "website:effectivedate")
@Node(jcrType = "website:effectivedate")
public class EffectiveDate extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:status")
    public String getStatus() {
        return getProperty("website:status");
    }

    @HippoEssentialsGenerated(internalName = "website:startdate")
    public Calendar getStartDate() {
        return getProperty("website:startdate");
    }

    @HippoEssentialsGenerated(internalName = "website:enddate")
    public Calendar getEndDate() {
        return getProperty("website:enddate");
    }

}
