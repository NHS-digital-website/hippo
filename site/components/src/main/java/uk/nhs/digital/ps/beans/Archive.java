package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;

@HippoEssentialsGenerated(internalName = "publicationsystem:archive")
@Node(jcrType = "publicationsystem:archive")

public class Archive extends BaseDocument {

    public HippoBean getSelfLinkBean() {
        return getCanonicalBean().getParentBean();
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Title")
    public String getTitle() {
        return getProperty("publicationsystem:Title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:Summary")
    public StructuredText getSummary() {
        return new StructuredText(getProperty("publicationsystem:Summary"));
    }
}
