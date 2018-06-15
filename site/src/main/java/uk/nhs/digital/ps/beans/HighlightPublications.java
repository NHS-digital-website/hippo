package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoDocument;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:highlightPublications")
@Node(jcrType = "publicationsystem:highlightPublications")
public class HighlightPublications extends HippoDocument {

    @HippoEssentialsGenerated(internalName = "publicationsystem:highlightSections")
    public List<HighlightSection> getHighlightSections() {
        return getChildBeansByName("publicationsystem:highlightSections",
            HighlightSection.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:relatedAreas")
    public HippoHtml getRelatedAreas() {
        return getHippoHtml("publicationsystem:relatedAreas");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:freedomOfInformation")
    public HippoHtml getFreedomOfInformation() {
        return getHippoHtml("publicationsystem:freedomOfInformation");
    }

}
