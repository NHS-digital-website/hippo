package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:section")
@Node(jcrType = "website:section")
public class Section extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:type")
    public String getType() {
        return getProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:numberedList")
    public boolean getIsNumberedList() {
        return getProperty("website:numberedList");
    }

    @HippoEssentialsGenerated(internalName = "website:html")
    public HippoHtml getHtml() {
        return getHippoHtml("website:html");
    }

    public String getSectionType() {
        return "website-section";
    }
}
