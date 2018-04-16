package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "publicationsystem:textSection")
@Node(jcrType = "publicationsystem:textSection")
public class TextSection extends HippoCompound {
    @HippoEssentialsGenerated(internalName = "publicationsystem:heading")
    public String getHeading() {
        return getProperty("publicationsystem:heading");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:text")
    public HippoHtml getText() {
        return getHippoHtml("publicationsystem:text");
    }

    public String getSectionType() {
        return "text";
    }
}
