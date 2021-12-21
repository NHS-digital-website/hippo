package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicationsystemResourcelink extends PublicationsystemResourceOrExternalLink {

    public PublicationsystemResourcelink(@JsonProperty(value = "link_text_REQ", required = true) String linktextReq,
                                        @JsonProperty(value = "link_url_REQ", required = true) String linkurlReq) {
        super(linktextReq, linkurlReq);
    }
}
