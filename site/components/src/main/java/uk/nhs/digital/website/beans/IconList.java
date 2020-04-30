package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.indices.StickySection;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:iconlist")
@Node(jcrType = "website:iconlist")
public class IconList extends BaseCompound implements StickySection {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @Override
    public String getHeading() {
        return this.getTitle();
    }

    @Override
    @HippoEssentialsGenerated(internalName = "website:headinglevel", allowModifications = false)
    public String getHeadingLevel() {
        return getSingleProperty("website:headinglevel");
    }

    @HippoEssentialsGenerated(internalName = "website:introduction")
    public HippoHtml getIntroduction() {
        return getHippoHtml("website:introduction");
    }

    @HippoEssentialsGenerated(internalName = "website:iconlistitems")
    public List<HippoBean> getIconListItems() {
        return getChildBeansByName("website:iconlistitems");
    }

    public String getSectionType() {
        return "iconList";
    }

}
