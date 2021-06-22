package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:svg")
@Node(jcrType = "website:svg")
public class SvgSection extends HippoCompound {

    public String getSectionType() {
        return "svg";
    }

    @HippoEssentialsGenerated(internalName = "website:altText")
    public String getAltText() {
        return getSingleProperty("website:altText");
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public CorporateWebsiteImageset getLink() {
        return getLinkedBean("website:link", CorporateWebsiteImageset.class);
    }

}
