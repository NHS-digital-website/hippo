package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:quotehero")
@Node(jcrType = "website:quotehero")
public class QuoteHero extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:quote")
    public String getQuote() {
        return getSingleProperty("website:quote");
    }

    @HippoEssentialsGenerated(internalName = "website:categoryInfo")
    public String getCategoryInfo() {
        return getSingleProperty("website:categoryInfo");
    }

    @HippoEssentialsGenerated(internalName = "website:jobRole")
    public String getJobRole() {
        return getSingleProperty("website:jobRole");
    }

    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getSingleProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:organisation")
    public String getOrganisation() {
        return getSingleProperty("website:organisation");
    }

    @HippoEssentialsGenerated(internalName = "website:image")
    public CorporateWebsiteImageset getImage() {
        return getLinkedBean("website:image", CorporateWebsiteImageset.class);
    }
}
