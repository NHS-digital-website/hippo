package uk.nhs.digital.ps.beans;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@HippoEssentialsGenerated(internalName = "publicationsystem:cilanding")
@Node(jcrType = "publicationsystem:cilanding")
public class CiLanding extends BaseDocument {
    @HippoEssentialsGenerated(internalName = "publicationsystem:title")
    public String getTitle() {
        return getProperty("publicationsystem:title");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:content")
    public HippoHtml getContent() {
        return getHippoHtml("publicationsystem:content");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:resourceLinks")
    public List<Relatedlink> getResourceLinks() {
        return getChildBeansByName("publicationsystem:resourceLinks",
                Relatedlink.class);
    }
}
