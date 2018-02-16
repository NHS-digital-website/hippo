package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.ps.beans.structuredText.StructuredText;

@HippoEssentialsGenerated(internalName = "publicationsystem:publication")
@Node(jcrType = "publicationsystem:publication")
public class Publication extends PublicationBase {

    @HippoEssentialsGenerated(internalName = PublicationBase.PropertyKeys.SUMMARY)
    public StructuredText getSummary() {
        assertPropertyPermitted(PublicationBase.PropertyKeys.SUMMARY);

        return new StructuredText(getProperty(PublicationBase.PropertyKeys.SUMMARY, ""));
    }

    @HippoEssentialsGenerated(internalName = PublicationBase.PropertyKeys.KEY_FACTS)
    public StructuredText getKeyFacts() {
        assertPropertyPermitted(PublicationBase.PropertyKeys.KEY_FACTS);

        return new StructuredText(getProperty(PublicationBase.PropertyKeys.KEY_FACTS, ""));
    }
}
