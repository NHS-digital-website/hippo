package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:cybercveidentifier")
@Node(jcrType = "website:cybercveidentifier")
public class CyberCveIdentifier extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:cveidentifier")
    public String getCveIdentifier() {
        return getProperty("website:cveidentifier");
    }

    @HippoEssentialsGenerated(internalName = "website:cvestatus")
    public String getCveStatus() {
        return getProperty("website:cvestatus");
    }

    @HippoEssentialsGenerated(internalName = "website:cvetext")
    public HippoHtml getCveText() {
        return getHippoHtml("website:cvetext");
    }

}
