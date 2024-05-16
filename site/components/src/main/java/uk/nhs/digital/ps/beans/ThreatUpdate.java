package uk.nhs.digital.ps.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.hippoecm.hst.content.beans.standard.HippoHtml;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@HippoEssentialsGenerated(internalName = "website:threatupdate")
@Node(jcrType = "website:threatupdate")
public class ThreatUpdate extends HippoCompound {

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:title")
    public String getTitle() {
        return getSingleProperty("website:title");
    }

    @HippoEssentialsGenerated(internalName = "website:content")
    public HippoHtml getContent() {
        return getHippoHtml("website:content");
    }

    @JsonProperty("content")
    public String getContentJson() {
        HippoHtml html = getContent();
        if (html != null) {
            return html.getContent();
        }
        return null;
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:date")
    public Calendar getDate() {
        return getSingleProperty("website:date");
    }

}
