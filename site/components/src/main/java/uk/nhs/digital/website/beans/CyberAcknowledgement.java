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
@HippoEssentialsGenerated(internalName = "website:cyberacknowledgement")
@Node(jcrType = "website:cyberacknowledgement")
public class CyberAcknowledgement extends HippoCompound {

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:linkaddress")
    public String getLinkAddress() {
        return getSingleProperty("website:linkaddress");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:responsedatetime")
    public Calendar getResponseDatetime() {
        return getSingleProperty("website:responsedatetime");
    }

}
