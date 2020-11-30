package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:graphicblock")
@Node(jcrType = "website:graphicblock")
public class GraphicBlock extends CommonFieldsBean {

    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:infographic")
    public Infographic getInfographics() {
        return getBean("website:infographic", Infographic.class);
    }
}
