package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:othersocialmedia")
@Node(jcrType = "website:othersocialmedia")
public class OtherSocialMedia extends HippoCompound {

    public String getSectionType() {
        return "othersocialmedia";
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public String getLink() {
        return getSingleProperty("website:link");
    }

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

}

