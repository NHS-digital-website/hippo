package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;

import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:biography")
@Node(jcrType = "website:biography")
public class Biography extends HippoCompound {

    public String getSectionType() {
        return "biography";
    }

    @HippoEssentialsGenerated(internalName = "website:profbiography")
    public HippoHtml getProfbiography() {
        return getHippoHtml("website:profbiography");
    }

    @HippoEssentialsGenerated(internalName = "website:prevpositions")
    public HippoHtml getPrevpositions() {
        return getHippoHtml("website:prevpositions");
    }

    @HippoEssentialsGenerated(internalName = "website:nonnhspositions")
    public HippoHtml getNonnhspositions() {
        return getHippoHtml("website:nonnhspositions");
    }

    @HippoEssentialsGenerated(internalName = "website:additionalbiography")
    public HippoHtml getAdditionalbiography() {
        return getHippoHtml("website:additionalbiography");
    }

    @HippoEssentialsGenerated(internalName = "website:personalbiography")
    public HippoHtml getPersonalbiography() {
        return getHippoHtml("website:personalbiography");
    }
}

