package uk.nhs.digital.website.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hippoecm.hst.content.beans.Node;
import org.hippoecm.hst.content.beans.standard.HippoBean;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

import java.util.List;

@JsonAutoDetect(getterVisibility = JsonAutoDetect.Visibility.NONE)
@HippoEssentialsGenerated(internalName = "website:definedterms")
@Node(jcrType = "website:definedterms")
public class DefinedTerms extends BaseDocument {

    @JsonProperty
    @HippoEssentialsGenerated(internalName = "website:terms")
    public List<HippoBean> getTerms() {
        return getChildBeansByName("website:terms");
    }

}
