package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:glossaryitem")
@Node(jcrType = "website:glossaryitem")
public class GlossaryItem extends BaseCompound {

    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getTitle() {
        return getSingleProperty("website:heading");
    }

}
