package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.BasicBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicationsystemResourceOrExternalLink extends BasicBodyItem {

    @JsonProperty("link_text_REQ")
    private String linkTextReq;
    @JsonProperty("link_url_REQ")
    private String linkUrlReq;

    public PublicationsystemResourceOrExternalLink(@JsonProperty(value = "link_text_REQ", required = true) String linktextReq,
                                                   @JsonProperty(value = "link_url_REQ", required = true) String linkurlReq) {
        this.linkTextReq = linktextReq;
        this.linkUrlReq = linkurlReq;
    }

    @JsonProperty("link_text_REQ")
    public String getLinkTextReq() {
        return linkTextReq;
    }

    @JsonProperty("link_text_REQ")
    public void setLinkTextReq(String linkTextReq) {
        this.linkTextReq = linkTextReq;
    }

    @JsonProperty("link_url_REQ")
    public String getLinkUrlReq() {
        return linkUrlReq;
    }

    @JsonProperty("link_url_REQ")
    public void setLinkUrlReq(String linkUrlReq) {
        this.linkUrlReq = linkUrlReq;
    }

}
