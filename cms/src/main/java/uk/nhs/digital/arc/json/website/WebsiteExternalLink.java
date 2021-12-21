package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteExternalLink extends AbstractWebsiteItemlink {

    @JsonProperty(value = "title_REQ", required = true)
    private String titleReq;

    @JsonProperty("short_summary_REQ")
    private String shortsummaryReq;

    @JsonProperty("link_REQ")
    private String linkReq;

    public WebsiteExternalLink(@JsonProperty(value = "title_REQ", required = true) String titleReq,
                               @JsonProperty(value = "short_summary_REQ", required = true) String shortsummaryReq,
                               @JsonProperty(value = "link_REQ", required = true) String linkReq) {
        this.titleReq = titleReq;
        this.shortsummaryReq = shortsummaryReq;
        this.linkReq = linkReq;
    }

    public String getTitleReq() {
        return titleReq;
    }

    public void setTitleReq(String titleReq) {
        this.titleReq = titleReq;
    }

    public String getShortsummaryReq() {
        return shortsummaryReq;
    }

    public void setShortsummaryReq(String shortsummaryReq) {
        this.shortsummaryReq = shortsummaryReq;
    }

    public String getLinkReq() {
        return linkReq;
    }

    public void setLinkReq(String linkReq) {
        this.linkReq = linkReq;
    }
}