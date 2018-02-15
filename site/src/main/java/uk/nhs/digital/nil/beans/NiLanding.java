package uk.nhs.digital.nil.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import uk.nhs.digital.ps.beans.BaseDocument;

@HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:nilanding")
@Node(jcrType = "nationalindicatorlibrary:nilanding")
public class NiLanding extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:title")
    public String getTitle() {
        return getProperty("nationalindicatorlibrary:title");
    }

    @HippoEssentialsGenerated(internalName = "nationalindicatorlibrary:content")
    public HippoHtml getContent() {
        return getHippoHtml("nationalindicatorlibrary:content");
    }

}
