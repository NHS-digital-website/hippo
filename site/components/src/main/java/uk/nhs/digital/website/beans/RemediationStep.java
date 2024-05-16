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
@HippoEssentialsGenerated(internalName = "website:remediationstep")
@Node(jcrType = "website:remediationstep")
public class RemediationStep extends HippoCompound {

    @JsonProperty("step")
    public String getStepJson() {
        HippoHtml html = getStep();
        if (html != null) {
            return html.getContent();
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:step")
    public HippoHtml getStep() {
        return getHippoHtml("website:step");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:link")
    public String getLink() {
        return getSingleProperty("website:link");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:type")
    public String getType() {
        return getSingleProperty("website:type");
    }

}
