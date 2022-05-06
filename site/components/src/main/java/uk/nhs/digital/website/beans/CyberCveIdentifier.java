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
@HippoEssentialsGenerated(internalName = "website:cybercveidentifier")
@Node(jcrType = "website:cybercveidentifier")
public class CyberCveIdentifier extends HippoCompound {

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:cveidentifier")
    public String getCveIdentifier() {
        return getSingleProperty("website:cveidentifier");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:cvestatus")
    public String getCveStatus() {
        return getSingleProperty("website:cvestatus");
    }

    @JsonProperty("cveText")
    public String getCveTextJson() {
        HippoHtml html = getCveText();
        if (html != null) {
            return html.getContent();
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:cvetext")
    public HippoHtml getCveText() {
        return getHippoHtml("website:cvetext");
    }

}
