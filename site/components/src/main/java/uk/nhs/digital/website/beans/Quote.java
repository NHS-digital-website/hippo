package uk.nhs.digital.website.beans;


import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:quote")
@Node(jcrType = "website:quote")
public class Quote extends BaseCompound {

    public String getSectionType() {
        return "quote";
    }

    @Override
    public String getTitle() {
        return null;
    }
}
