package uk.nhs.digital.arc.json;

import com.fasterxml.jackson.annotation.*;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Page {

    @JsonProperty("document_type")
    private String documentType;

    @JsonProperty("page_name")
    private String pageName;
    @JsonProperty("page_ref")
    private String pageRef;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<>();

    @JsonProperty("document_type")
    public String getDocumentType() {
        return documentType;
    }

    @JsonProperty("document_type")
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    @JsonProperty("page_name")
    public String getPageName() {
        return pageName;
    }

    @JsonProperty("page_name")
    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    @JsonProperty("page_ref")
    public String getPageRef() {
        return pageRef;
    }

    @JsonProperty("page_ref")
    public void setPageRef(String pageRef) {
        this.pageRef = pageRef;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}