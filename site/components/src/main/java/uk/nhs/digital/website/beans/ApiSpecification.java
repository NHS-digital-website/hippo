package uk.nhs.digital.website.beans;

import org.hippoecm.hst.content.beans.Node;
import org.onehippo.cms7.essentials.dashboard.annotations.HippoEssentialsGenerated;
import uk.nhs.digital.apispecs.swagger.SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter;

@Node(jcrType = "website:apispecification")
@HippoEssentialsGenerated(internalName = "website:apispecification", allowModifications = false)
public class ApiSpecification extends CommonFieldsBean {

    public String getSpecificationid() {
        return getSingleProperty("website:specification_id");
    }

    public String getHtml() {
        final String json = getJson();
        final SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter converter = new SwaggerCodeGenOpenApiSpecificationJsonToHtmlConverter();
        return converter.htmlFrom(json);
    }

    public String getJson() {
        return getSingleProperty("website:json");
    }
}
