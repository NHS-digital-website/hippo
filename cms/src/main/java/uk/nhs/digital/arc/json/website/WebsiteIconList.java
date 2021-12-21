package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteIconList extends PublicationBodyItem {

    @JsonProperty("title")
    private String title;
    @JsonProperty("heading_level_REQ")
    private String headinglevelReq;
    @JsonProperty("introduction")
    private String introduction;
    @JsonProperty("iconlist_items")
    private List<WebsiteIconListItem> iconlistitems = null;

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("heading_level_REQ")
    public String getHeadinglevelReq() {
        return headinglevelReq;
    }

    @JsonProperty("heading_level_REQ")
    public void setHeadinglevelReq(String headinglevelReq) {
        this.headinglevelReq = headinglevelReq;
    }

    @JsonProperty("introduction")
    public String getIntroduction() {
        return introduction;
    }

    @JsonProperty("introduction")
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    @JsonProperty("iconlist_items")
    public List<WebsiteIconListItem> getIconlistitems() {
        return iconlistitems;
    }

    @JsonProperty("iconlist_items")
    public void setIconlistitems(List<WebsiteIconListItem> iconlistitems) {
        this.iconlistitems = iconlistitems;
    }
}