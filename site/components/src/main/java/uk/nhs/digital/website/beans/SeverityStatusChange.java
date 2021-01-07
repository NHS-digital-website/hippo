package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.Calendar;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@HippoEssentialsGenerated(internalName = "website:severitystatuschange")
@Node(jcrType = "website:severitystatuschange")
public class SeverityStatusChange extends HippoCompound {

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:severity", allowModifications = false)
    public String getSeverity() {
        return getSingleProperty("website:severity");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:date")
    public Calendar getDate() {
        return getSingleProperty("website:date");
    }

}
