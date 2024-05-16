package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:responsedefinition")
@Node(jcrType = "website:responsedefinition")
public class ResponseDefinition extends BaseCompound {

    public String getSectionType() {
        return "responsedefinition";
    }

    @Override
    public String getTitle() {
        return null;
    }
}
