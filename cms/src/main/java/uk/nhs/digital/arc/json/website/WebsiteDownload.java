package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteDownload extends PublicationBodyItem {

    @JsonProperty("heading_REQ")
    private String headingReq;
    @JsonProperty("items")
    private List<AbstractWebsiteItemlink> items = null;
    @JsonProperty("heading_level_REQ")
    private String headingLevelReq;
    @JsonProperty("description")
    private String description;

    public WebsiteDownload(@JsonProperty(value = "heading_REQ", required = true)String headingReq,
                           @JsonProperty(value = "heading_level_REQ", required = true)String headingLevelReq) {
        this.headingLevelReq = headingLevelReq;
        this.headingReq = headingReq;
    }

    @JsonProperty("heading_REQ")
    public String getHeadingReq() {
        return headingReq;
    }

    @JsonProperty("heading_REQ")
    public void setHeadingReq(String headingReq) {
        this.headingReq = headingReq;
    }

    @JsonProperty("items")
    public List<AbstractWebsiteItemlink> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<AbstractWebsiteItemlink> items) {
        this.items = items;
    }

    @JsonProperty("heading_level")
    public String getHeadingLevel() {
        return headingLevelReq;
    }

    @JsonProperty("heading_level")
    public void setHeadingLevel(String headingLevel) {
        this.headingLevelReq = headingLevel;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

}