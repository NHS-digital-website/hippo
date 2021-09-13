package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:gdprrights")
@Node(jcrType = "website:gdprrights")
public class GdprRights extends HippoCompound {


    @HippoEssentialsGenerated(internalName = "website:qualification")
    public HippoHtml getQualification() {
        return getHippoHtml("website:qualification");
    }

    @HippoEssentialsGenerated(internalName = "website:gdprrightsyesno")
    public String getGdprRightRequired() {
        return getSingleProperty("website:gdprrightsyesno");
    }

}
