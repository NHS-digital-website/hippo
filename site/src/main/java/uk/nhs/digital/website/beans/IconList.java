package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:iconlist")
@Node(jcrType = "website:iconlist")
public class IconList extends BaseCompound {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:iconlistitems")
    public List<HippoBean> getIconListItems() {
        return getChildBeansByName("website:iconlistitems");
    }

    public String getSectionType() {
        return "iconList";
    }

}
