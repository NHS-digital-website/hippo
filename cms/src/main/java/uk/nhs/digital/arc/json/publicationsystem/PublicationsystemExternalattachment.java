package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.nhs.digital.arc.json.BasicBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("external_attachment")
public class PublicationsystemExternalattachment extends BasicBodyItem {

    @JsonProperty("display_name")
    private String displayName;
    @JsonProperty("resource")
    private String resource;

    @JsonProperty("display_name")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("display_name")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonProperty("resource")
    public String getResource() {
        return resource;
    }

    @JsonProperty("resource")
    public void setResource(String resource) {
        this.resource = resource;
    }

}
