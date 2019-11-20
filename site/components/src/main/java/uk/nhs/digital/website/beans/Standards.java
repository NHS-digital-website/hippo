package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:standards")
@Node(jcrType = "website:standards")
public class Standards extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:body")
    public String getBody() {
        return getProperty("website:body");
    }

    @HippoEssentialsGenerated(internalName = "website:name")
    public String getName() {
        return getProperty("website:name");
    }

    @HippoEssentialsGenerated(internalName = "website:referencenumber")
    public String getReferenceNumber() {
        return getProperty("website:referencenumber");
    }

    @HippoEssentialsGenerated(internalName = "website:weblink")
    public String getWebLink() {
        return getProperty("website:weblink");
    }

}
