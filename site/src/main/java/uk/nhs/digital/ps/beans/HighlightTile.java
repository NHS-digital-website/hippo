package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoMirror;
import org.hippoecm.hst.content.beans.standard.HippoResource;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "publicationsystem:highlightTile")
@Node(jcrType = "publicationsystem:highlightTile")
public class HighlightTile extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "publicationsystem:image")
    public HippoResource getImage() {
        return getChildBeansByName("publicationsystem:image", HippoResource.class).get(0);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:summary")
    public String getSummary() {
        return getProperty("publicationsystem:summary");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:publicationLink")
    public HippoMirror getPublicationLink() {
        return getChildBeansByName("publicationsystem:publicationLink", HippoMirror.class).get(0);
    }

}
