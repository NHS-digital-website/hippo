package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.indices.StickySection;

@HippoEssentialsGenerated(internalName = "publicationsystem:textSection")
@Node(jcrType = "publicationsystem:textSection")
public class TextSection extends HippoCompound implements StickySection {

    @Override
    @HippoEssentialsGenerated(internalName = "publicationsystem:heading", allowModifications = false)
    public String getHeading() {
        return getSingleProperty("publicationsystem:heading");
    }

    @Override
    public String getHeadingLevel() {
        return "Main Heading";
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:text")
    public HippoHtml getText() {
        return getHippoHtml("publicationsystem:text");
    }

    public String getSectionType() {
        return "text";
    }
}
