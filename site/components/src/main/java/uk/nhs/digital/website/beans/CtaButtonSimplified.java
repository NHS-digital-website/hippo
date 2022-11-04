package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "website:ctaButtonSimplified")
@Node(jcrType = "website:ctaButtonSimplified")
public class CtaButtonSimplified extends HippoCompound {

    public String getSectionType() {
        return "ctaButtonSimplified";
    }

    @HippoEssentialsGenerated(internalName = "website:items")
    public List<?> getItems() {
        return getChildBeansByName("website:items");
    }

}
