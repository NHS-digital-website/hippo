package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.indices.StickySection;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:download")
@Node(jcrType = "website:download")
public class Download extends HippoCompound implements StickySection {

    public String getTitle() {
        return getHeading();
    }

    @Override
    @HippoEssentialsGenerated(internalName = "website:heading", allowModifications = false)
    public String getHeading() {
        return getSingleProperty("website:heading");
    }

    @Override
    @HippoEssentialsGenerated(internalName = "website:headinglevel", allowModifications = false)
    public String getHeadingLevel() {
        return getSingleProperty("website:headinglevel");
    }

    @HippoEssentialsGenerated(internalName = "website:description")
    public HippoHtml getDescription() {
        return getHippoHtml("website:description");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<?> getItems() {
        return getChildBeansByName("website:items");
    }

    public String getSectionType() {
        return "download";
    }

}
