package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:owner")
@Node(jcrType = "website:owner")
public class Owner extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "website:department")
    public String getDepartment() {
        return getSingleProperty("website:department");
    }

    @HippoEssentialsGenerated(internalName = "website:email")
    public String getEmail() {
        return getSingleProperty("website:email");
    }

    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getSingleProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:telephone")
    public String getTelephone() {
        return getSingleProperty("website:telephone");
    }
}
