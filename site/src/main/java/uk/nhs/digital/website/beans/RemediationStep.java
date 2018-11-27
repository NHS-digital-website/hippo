package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:remediationstep")
@Node(jcrType = "website:remediationstep")
public class RemediationStep extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:step")
    public HippoHtml getStep() {
        return getHippoHtml("website:step");
    }

    @HippoEssentialsGenerated(internalName = "website:link")
    public String getLink() {
        return getProperty("website:link");
    }

    @HippoEssentialsGenerated(internalName = "website:type")
    public String getType() {
        return getProperty("website:type");
    }

}
