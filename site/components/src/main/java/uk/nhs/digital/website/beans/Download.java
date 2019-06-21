package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;


@HippoEssentialsGenerated(internalName = "website:download")
@Node(jcrType = "website:download")
public class Download extends HippoCompound {

    public String getTitle() {
        return getHeading();
    }

    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:headinglevel")
    public String getHeadingLevel() {
        return getProperty("website:headinglevel");
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
