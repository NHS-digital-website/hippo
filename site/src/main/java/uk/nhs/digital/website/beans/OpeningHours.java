package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;
import java.util.List;

@HippoEssentialsGenerated(internalName = "website:openinghours")
@Node(jcrType = "website:openinghours")
public class OpeningHours extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:day")
    public List<HippoBean> getDay() {
        return getChildBeansByName("website:day");
    }

    @HippoEssentialsGenerated(internalName = "website:starttime")
    public Calendar getStarttime() {
        return getProperty("website:starttime");
    }

    @HippoEssentialsGenerated(internalName = "website:endtime")
    public Calendar getEndtime() {
        return getProperty("website:endtime");
    }

    public String getSectionType() {
        return "openinghours";
    }

}
