package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:ctabutton")
@Node(jcrType = "website:ctabutton")
public class CtaButton extends HippoCompound {

    public String getSectionType() {
        return "ctabutton";
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:buttontype")
    public String getButtonType() {
        return getSingleProperty("website:buttontype");
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<?> getItems() {
        return getChildBeansByName("website:items");
    }

    @HippoEssentialsGenerated(internalName = "website:alignment")
    public String getAlignment() {
        return getSingleProperty("website:alignment");
    }
}
