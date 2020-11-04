package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@Node(jcrType = "website:video")
public class Video extends CommonFieldsBean {
    @HippoEssentialsGenerated(internalName = "website:title", allowModifications = false)
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:seosummary", allowModifications = false)
    public HippoHtml getSeosummary() {
        return getHippoHtml("website:seosummary");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:shortsummary", allowModifications = false)
    public String getShortsummary() {
        return getSingleProperty("website:shortsummary");
    }

    @HippoEssentialsGenerated(internalName = "website:introduction", allowModifications = false)
    public HippoHtml getIntroduction() {
        return getHippoHtml("website:introduction");
    }

    @HippoEssentialsGenerated(internalName = "website:externallinkbase")
    public String getVideouri() {
        return getSingleProperty("website:externallinkbase");
    }

    @HippoEssentialsGenerated(internalName = "website:relateddocument")
    public HippoBean getRelateddocument() {
        return getLinkedBean("website:relateddocument", HippoBean.class);
    }
}
