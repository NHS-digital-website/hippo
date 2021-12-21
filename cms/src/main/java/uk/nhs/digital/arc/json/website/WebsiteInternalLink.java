package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteInternalLink extends AbstractWebsiteItemlink {

    @JsonProperty("link_REQ")
    private String linkReq;

    public WebsiteInternalLink(@JsonProperty(value = "link_REQ", required = true) String linkReq) {
        this.linkReq = linkReq;
    }

    @JsonProperty("link_REQ")
    public String getLinkReq() {
        return linkReq;
    }

    @JsonProperty("link_REQ")
    public void setLinkReq(String linkReq) {
        this.linkReq = linkReq;
    }

}