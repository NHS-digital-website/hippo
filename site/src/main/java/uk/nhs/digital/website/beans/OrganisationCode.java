package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:organisationcode")
@Node(jcrType = "website:organisationcode")
public class OrganisationCode extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:type")
    public String getType() {
        return getProperty("website:type");
    }

    @HippoEssentialsGenerated(internalName = "website:code")
    public String getCode() {
        return getProperty("website:code");
    }

    @HippoEssentialsGenerated(internalName = "website:url")
    public String getUrl() {
        return getProperty("website:url");
    }

}
