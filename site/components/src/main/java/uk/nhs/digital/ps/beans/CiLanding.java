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

    @HippoEssentialsGenerated(internalName = "publicationsystem:cilandingasset")
    public List<CiLandingAsset> getSubSections() {
        return getChildBeansByName("publicationsystem:cilandingasset",
                CiLandingAsset.class);
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:actionLinkName")
    public String getActionLinkName() {
        return getProperty("publicationsystem:actionLinkName");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:actionLinkRelPath")
    public String getActionLinkRelPath() {
        return getProperty("publicationsystem:actionLinkRelPath");
    }

    @HippoEssentialsGenerated(internalName = "publicationsystem:urlNameOfContentFolder")
    public String getUrlNameOfContentFolder() {
        return getProperty("publicationsystem:urlNameOfContentFolder");
    }

}
