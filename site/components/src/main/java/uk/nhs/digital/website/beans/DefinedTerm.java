package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoCompound;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@HippoEssentialsGenerated(internalName = "website:definedterm")
@Node(jcrType = "website:definedterm")
public class DefinedTerm extends HippoCompound {

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:term")
    public String getTerm() {
        return getSingleProperty("website:term");
    }

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:colorrgb")
    public String getColorrgb() {
        return getSingleProperty("website:colorrgb");
    }
}
