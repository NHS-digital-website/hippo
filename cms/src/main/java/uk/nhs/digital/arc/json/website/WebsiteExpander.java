package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteExpander extends PublicationBodyItem {

    @JsonProperty("heading_REQ")
    private String headingReq;
    @JsonProperty("audience_REQ")
    private String audienceReq;
    @JsonProperty("content")
    private String content;

    public WebsiteExpander(@JsonProperty(value = "heading_REQ", required = true) String headingReq,
                           @JsonProperty(value = "audience_REQ", required = true) String audienceReq) {
        this.headingReq = headingReq;
        this.audienceReq = audienceReq;
    }

    @JsonProperty("heading_REQ")
    public String getHeadingReq() {
        return headingReq;
    }

    @JsonProperty("heading_REQ")
    public void setHeadingReq(String headingReq) {
        this.headingReq = headingReq;
    }

    @JsonProperty("audience_REQ")
    public String getAudienceReq() {
        return audienceReq;
    }

    @JsonProperty("audience_REQ")
    public void setAudienceReq(String audienceReq) {
        this.audienceReq = audienceReq;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }
}