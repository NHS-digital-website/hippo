package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;

@Node(jcrType = "website:apispecification")
@HippoEssentialsGenerated(internalName = "website:apispecification", allowModifications = false)
public class ApiSpecification extends CommonFieldsBean {

    public String getSpecificationId() {
        return getSingleProperty("website:specification_id");
    }

    public Boolean getShowApiResult() {
        return getSingleProperty("website:showApiResult");
    }

    public Boolean getEnableRapiDoc() {
        return getSingleProperty("website:enable_rapidoc");
    }

    public String getHtml() {
        return getSingleProperty("website:html");
    }

    public String getJson() {
        return getSingleProperty("website:json");
    }
}
