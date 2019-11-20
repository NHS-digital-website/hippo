package uk.nhs.digital.common.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "common:about")
@Node(jcrType = "common:about")
public class About extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "common:Title")
    public String getTitle() {
        return getProperty("common:Title");
    }

    @HippoEssentialsGenerated(internalName = "common:Content")
    public HippoHtml getContent() {
        return getHippoHtml("common:Content");
    }
}
