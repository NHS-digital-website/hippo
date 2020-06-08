package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@HippoEssentialsGenerated(internalName = "website:emphasisBox")
@Node(jcrType = "website:emphasisBox")
public class EmphasisBox extends HippoCompound {

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:emphasisType")
    public String getEmphasisType() {
        return getSingleProperty("website:emphasisType");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getSingleProperty("website:heading");
    }

    @JsonProperty("body")
    public String getBodyJson() {
        HippoHtml html = getBody();
        if (html != null) {
            return html.getContent();
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:body")
    public HippoHtml getBody() {
        return getHippoHtml("website:body");
    }

    @JsonProperty
    public String getSectionType() {
        return "emphasisBox";
    }

    @JsonIgnore
    @HippoEssentialsGenerated(internalName = "website:image")
    public CorporateWebsiteImageset getImage() {
        return getLinkedBean("website:image", CorporateWebsiteImageset.class);
    }

}
