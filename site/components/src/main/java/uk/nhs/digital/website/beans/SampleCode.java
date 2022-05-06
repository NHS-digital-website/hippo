package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@HippoEssentialsGenerated(internalName = "website:samplecode")
@Node(jcrType = "website:samplecode")
public class SampleCode extends HippoCompound {

    @HippoEssentialsGenerated(internalName = "website:webcode")
    public Code getWebcode() {
        return getBean("website:webcode", Code.class);
    }

    @HippoEssentialsGenerated(internalName = "website:content")
    public HippoHtml getContent() {
        return getHippoHtml("website:content");
    }

    public String getSectionType() {
        return "samplecode";
    }
}
