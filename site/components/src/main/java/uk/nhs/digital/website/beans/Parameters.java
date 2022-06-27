package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:parameters")
@Node(jcrType = "website:parameters")
public class Parameters extends BaseCompound {


    public String getSectionType() {
        return "parameters";
    }

    @Override
    public String getTitle() {
        return null;
    }
}
