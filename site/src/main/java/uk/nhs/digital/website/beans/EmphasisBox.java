package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;


@HippoEssentialsGenerated(internalName = "website:emphasisBox")
@Node(jcrType = "website:emphasisBox")
public class EmphasisBox extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:emphasisType")
    public String getEmphasisType() {
        return getProperty("website:emphasisType");
    }

    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getProperty("website:heading");
    }

    @HippoEssentialsGenerated(internalName = "website:body")
    public HippoHtml getBody() {
        return getHippoHtml("website:body");
    }

    public String getSectionType() {
        return "emphasisBox";
    }

}
