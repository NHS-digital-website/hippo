package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;

@HippoEssentialsGenerated(internalName = "website:interval")
@Node(jcrType = "website:interval")
public class Interval extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:enddatetime")
    public Calendar getEnddatetime() {
        return getProperty("website:enddatetime");
    }

    @HippoEssentialsGenerated(internalName = "website:startdatetime")
    public Calendar getStartdatetime() {
        return getProperty("website:startdatetime");
    }
}
