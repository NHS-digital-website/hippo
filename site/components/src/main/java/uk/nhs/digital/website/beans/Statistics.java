package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.indices.StickySection;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:statistics")
@Node(jcrType = "website:statistics")
public class Statistics extends BaseCompound implements StickySection {

    public String getTitle() {
        return getHeading();
    }

    @Override
    @HippoEssentialsGenerated(internalName = "website:heading", allowModifications = false)
    public String getHeading() {
        return getSingleProperty("website:heading");
    }

    @Override
    @HippoEssentialsGenerated(internalName = "website:headingLevel", allowModifications = false)
    public String getHeadingLevel() {
        return getSingleProperty("website:headingLevel");
    }

    @HippoEssentialsGenerated(internalName = "website:colourScheme")
    public String getColourScheme() {
        return getSingleProperty("website:colourScheme");
    }

    @HippoEssentialsGenerated(internalName = "website:animation")
    public String getAnimation() {
        return getSingleProperty("website:animation");
    }

    @HippoEssentialsGenerated(internalName = "website:modules")
    public List<HippoBean> getModules() {
        return getChildBeansByName("website:modules");
    }

    public String getSectionType() {
        return "statisticsSection";
    }

}
