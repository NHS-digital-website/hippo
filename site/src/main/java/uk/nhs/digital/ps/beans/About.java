package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

@HippoEssentialsGenerated(internalName = "publicationsystem:about")
@Node(jcrType = "publicationsystem:about")
public class About extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getProperty("publicationsystem:Title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Content")
    public HippoHtml getContent() {
        return getHippoHtml("publicationsystem:Content");
    }
}
