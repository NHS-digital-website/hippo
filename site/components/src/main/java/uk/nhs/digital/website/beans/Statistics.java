package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:statistics")
@Node(jcrType = "website:statistics")
public class Statistics extends HippoCompound {

    public String getTitle() {
        return getHeading();
    }

    @HippoEssentialsGenerated(internalName = "website:heading", allowModifications = false)
    public String getHeading() {
        return getProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:headingLevel", allowModifications = false)
    public String getHeadingLevel() {
        return getProperty("website:headingLevel");
    }

    @HippoEssentialsGenerated(internalName = "website:colourScheme")
    public String getColourScheme() {
        return getProperty("website:colourScheme");
    }

    @HippoEssentialsGenerated(internalName = "website:animation")
    public String getAnimation() {
        return getProperty("website:animation");
    }

    @HippoEssentialsGenerated(internalName = "website:modules")
    public List<HippoBean> getModules() {
        return getChildBeansByName("website:modules");
    }

    public String getSectionType() {
        return "statisticsSection";
    }

}
