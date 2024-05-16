package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@HippoEssentialsGenerated(internalName = "website:expander")
@Node(jcrType = "website:expander")
public class Expander extends HippoCompound {

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:audience")
    public String getAudience() {
        return getSingleProperty("website:audience");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:heading")
    public String getHeading() {
        return getSingleProperty("website:heading");
    }

    @JsonProperty("content")
    public String getContentJson() {
        HippoHtml html = getContent();
        if (html != null) {
            return html.getContent();
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:content")
    public HippoHtml getContent() {
        return getHippoHtml("website:content");
    }

    @JsonProperty
    public String getSectionType() {
        return "expander";
    }
}
