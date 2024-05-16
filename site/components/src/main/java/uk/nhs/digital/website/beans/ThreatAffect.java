package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@HippoEssentialsGenerated(internalName = "website:threataffect")
@Node(jcrType = "website:threataffect")
public class ThreatAffect extends HippoCompound {

    @JsonProperty("platformText")
    public String getPlatformTextJson() {
        HippoHtml html = getPlatformText();
        if (html != null) {
            return html.getContent();
        }
        return null;
    }

    @HippoEssentialsGenerated(internalName = "website:platformtext")
    public HippoHtml getPlatformText() {
        return getHippoHtml("website:platformtext");
    }

    @JsonIgnore
    @HippoEssentialsGenerated(internalName = "website:platformaffected")
    public HippoBean getPlatformAffected() {
        return getLinkedBean("website:platformaffected", HippoBean.class);
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:versionsaffected")
    public String[] getVersionsAffected() {
        return getMultipleProperty("website:versionsaffected");
    }

}
