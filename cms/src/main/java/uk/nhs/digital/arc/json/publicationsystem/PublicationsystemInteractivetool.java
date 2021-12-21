package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.nhs.digital.arc.json.BasicBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("interactive_tool")
public class PublicationsystemInteractivetool extends BasicBodyItem {

    @JsonProperty("title_REQ")
    private String titleReq;
    @JsonProperty("link_REQ")
    private String linkReq;
    @JsonProperty("content")
    private String content;
    @JsonProperty("date")
    private String date;
    @JsonProperty("accessible_REQ")
    private String accessibleReq;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonProperty("accessible_location")
    private PublicationsystemAccessibleLocation accessibleLocation;

    public PublicationsystemInteractivetool(@JsonProperty(value = "title_REQ", required = true) String titleReq,
                                            @JsonProperty(value = "link_REQ", required = true) String linkReq,
                                            @JsonProperty(value = "accessible_REQ", required = true) String accessibleReq) {
        this.titleReq = titleReq;
        this.linkReq = linkReq;
        this.accessibleReq = accessibleReq;
    }

    @JsonProperty("title_REQ")
    public String getTitleReq() {
        return titleReq;
    }

    @JsonProperty("title_REQ")
    public void setTitleReq(String titleReq) {
        this.titleReq = titleReq;
    }

    @JsonProperty("link_REQ")
    public String getLinkReq() {
        return linkReq;
    }

    @JsonProperty("link_REQ")
    public void setLinkReq(String linkReq) {
        this.linkReq = linkReq;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("accessible_REQ")
    public String getAccessibleReq() {
        return accessibleReq;
    }

    @JsonProperty("accessible_REQ")
    public void setAccessibleReq(String accessibleReq) {
        this.accessibleReq = accessibleReq;
    }

    @JsonProperty("accessible_location")
    public PublicationsystemAccessibleLocation getAccessibleLocation() {
        return accessibleLocation;
    }

    @JsonProperty("accessible_location")
    public void setAccessibleLocation(PublicationsystemAccessibleLocation accessibleLocation) {
        this.accessibleLocation = accessibleLocation;
    }

}