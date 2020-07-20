package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:userNeed")
@Node(jcrType = "website:userNeed")
public class UserNeed extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:Ineedto")
    public String getIneedto() {
        return getSingleProperty("website:Ineedto");
    }

    @HippoEssentialsGenerated(internalName = "website:asA")
    public String getAsA() {
        return getSingleProperty("website:asA");
    }

    @HippoEssentialsGenerated(internalName = "website:soThat")
    public String getSoThat() {
        return getSingleProperty("website:soThat");
    }
}
