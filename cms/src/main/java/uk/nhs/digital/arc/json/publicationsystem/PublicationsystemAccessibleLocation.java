package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.nhs.digital.arc.json.BasicBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("accessible_location")
public class PublicationsystemAccessibleLocation extends BasicBodyItem {

    @JsonProperty("content")
    private String content;
    @JsonProperty("link_REQ")
    private String linkReq;
    @JsonProperty("title_REQ")
    private String titleReq;

    public PublicationsystemAccessibleLocation(@JsonProperty(value = "link_REQ", required = true)String linkReq,
                                               @JsonProperty(value = "title_REQ", required = true)String titleReq) {
        this.linkReq = linkReq;
        this.titleReq = titleReq;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty("link_REQ")
    public String getLinkReq() {
        return linkReq;
    }

    @JsonProperty("link_REQ")
    public void setLinkReq(String linkReq) {
        this.linkReq = linkReq;
    }

    @JsonProperty("title_REQ")
    public String getTitleReq() {
        return titleReq;
    }

    @JsonProperty("title_REQ")
    public void setTitleReq(String titleReq) {
        this.titleReq = titleReq;
    }
}