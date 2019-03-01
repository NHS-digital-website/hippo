package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:iconlist")
@Node(jcrType = "website:iconlist")
public class IconList extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:iconlist")
    public List<HippoBean> getIconList() {
        return getChildBeansByName("website:iconlist");
    }

    public String getSectionType() {
        return "iconList";
    }

}
