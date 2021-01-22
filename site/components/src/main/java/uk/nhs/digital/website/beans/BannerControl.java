package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:bannercontrol")
@Node(jcrType = "website:bannercontrol")
public class BannerControl extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:backgroundcolor")
    public String getBackgroundcolor() {
        return getSingleProperty("website:backgroundcolor");
    }

    @HippoEssentialsGenerated(internalName = "website:iconcolor")
    public String getIconcolor() {
        return getSingleProperty("website:iconcolor");
    }

    @HippoEssentialsGenerated(internalName = "website:fontcolor")
    public String getFontcolor() {
        return getSingleProperty("website:fontcolor");
    }

    @HippoEssentialsGenerated(internalName = "website:icon")
    public CorporateWebsiteImageset getIcon() {
        return getLinkedBean("website:icon", CorporateWebsiteImageset.class);
    }

}
