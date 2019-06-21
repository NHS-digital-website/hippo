package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:checklist")
@Node(jcrType = "website:checklist")
public class Checklist extends HippoCompound {

    public String getSectionType() {
        return "checklist";
    }

    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:icon")
    public String getIcon() {
        return getProperty("website:icon");
    }

    @HippoEssentialsGenerated(internalName = "website:customicon")
    public HippoGalleryImageSet getCustomicon() {
        return getLinkedBean("website:customicon", HippoGalleryImageSet.class);
    }

    @HippoEssentialsGenerated(internalName = "website:listentries")
    public List<HippoHtml> getListentries() {
        return getChildBeansByName("website:listentries", HippoHtml.class);
    }

}
