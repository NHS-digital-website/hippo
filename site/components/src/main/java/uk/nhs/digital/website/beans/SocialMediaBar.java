package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:socialMediaBar")
@Node(jcrType = "website:socialMediaBar")
public class SocialMediaBar extends HippoCompound {
    
    @HippoEssentialsGenerated(internalName = "website:enable")
    public Boolean getEnable() {
        return getSingleProperty("website:enable");
    }
    
    @HippoEssentialsGenerated(internalName = "website:direction")
    public Boolean getDirection() {
        return getSingleProperty("website:direction");
    }
    
    @HippoEssentialsGenerated(internalName = "website:hexagons")
    public Boolean getHexagons() {
        return getSingleProperty("website:hexagons");
    }
    
    @HippoEssentialsGenerated(internalName = "website:iconSize")
    public Boolean getIconSize() {
        return getSingleProperty("website:iconSize");
    }
}