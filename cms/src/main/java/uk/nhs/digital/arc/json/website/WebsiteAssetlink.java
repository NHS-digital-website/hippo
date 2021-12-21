package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteAssetlink extends AbstractWebsiteItemlink {

    @JsonProperty("filename_REQ")
    private String filenameReq;
    @JsonProperty("link_REQ")
    private String linkReq;

    @JsonProperty("filename_REQ")
    public String getFilenameReq() {
        return filenameReq;
    }

    @JsonProperty("filename_REQ")
    public void setFilenameReq(String filenameReq) {
        this.filenameReq = filenameReq;
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