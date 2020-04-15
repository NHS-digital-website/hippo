package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:navigationTile")
@Node(jcrType = "website:navigationTile")
public class NavigationTile extends HippoCompound {

    public String getSectionType() {
        return "navigationTile";
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:imageOrIcon")
    public HippoGalleryImageSet getImage() {
        return getLinkedBean("website:imageOrIcon", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public String getDescription() {
        return getProperty("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:actionDescription")
    public String getActionDescription() {
        return getProperty("website:actionDescription");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<HippoBean> getLink() {
        return getChildBeansByName("website:items");
    }
}
