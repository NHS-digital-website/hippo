package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:calltoactionrich")
@Node(jcrType = "website:calltoactionrich")
public class CallToActionRich extends Calltoaction {

    @HippoEssentialsGenerated(internalName = "website:content")
    public HippoHtml getRichContent() {
        return getHippoHtml("website:content");
    }
}
