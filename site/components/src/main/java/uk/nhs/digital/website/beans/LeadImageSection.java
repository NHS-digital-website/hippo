package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:leadimagesection")
@Node(jcrType = "website:leadimagesection")
public class LeadImageSection extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:image", allowModifications = false)
    public CorporateWebsiteImageset getLeadImage() {
        return getLinkedBean("website:image", CorporateWebsiteImageset.class);
    }

    @HippoEssentialsGenerated(internalName = "website:alttext", allowModifications = false)
    public String getAlttext() {
        return getSingleProperty("website:alttext");
    }

    @HippoEssentialsGenerated(internalName = "website:imagecaption", allowModifications = false)
    public HippoHtml getImagecaption() {
        return getHippoHtml("website:imagecaption");
    }


}
