package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:socialmedia")
@Node(jcrType = "website:socialmedia")
public class SocialMedia extends BaseCompound {

    public String getSectionType() {
        return "socialmedia";
    }


    @Override
    public String getTitle() {
        return null;
    }
}

