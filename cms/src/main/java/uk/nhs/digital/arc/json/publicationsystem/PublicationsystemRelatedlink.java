package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicationsystemRelatedlink extends PublicationsystemResourceOrExternalLink {

    public PublicationsystemRelatedlink(@JsonProperty(value = "link_text_REQ", required = true) String linkTextReq,
                                         @JsonProperty(value = "link_url_REQ", required = true) String linkUrlReq) {
        super(linkTextReq, linkUrlReq);
    }
}
