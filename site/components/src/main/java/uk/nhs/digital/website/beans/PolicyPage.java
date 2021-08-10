package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:policypage")
@Node(jcrType = "website:policypage")
public class PolicyPage extends HippoCompound {

    public String getSectionType() {
        return "policypage";
    }

}
