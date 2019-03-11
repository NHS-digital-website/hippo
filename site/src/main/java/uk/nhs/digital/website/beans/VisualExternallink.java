package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoGalleryImageSet;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:visualexternallink")
@Node(jcrType = "website:visualexternallink")
public class VisualExternallink extends Externallink {

    @HippoEssentialsGenerated(internalName = "website:icon")
    public HippoGalleryImageSet getIcon() {
        return getLinkedBean("website:icon", HippoGalleryImageSet.class);
    }

}
