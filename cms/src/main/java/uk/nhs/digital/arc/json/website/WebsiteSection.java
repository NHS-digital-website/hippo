package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteSection extends PublicationBodyItem {

    @JsonProperty("title")
    private String title;
    @JsonProperty("heading_level")
    private String headingLevel;
    @JsonProperty("html")
    private String html;
    @JsonProperty("type")
    private String type;
    @JsonProperty("numbered_list")
    private String numberedList;

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("heading_level")
    public String getHeadingLevel() {
        return headingLevel;
    }

    @JsonProperty("heading_level")
    public void setHeadingLevel(String headingLevel) {
        this.headingLevel = headingLevel;
    }

    @JsonProperty("html")
    public String getHtml() {
        return html;
    }

    @JsonProperty("html")
    public void setHtml(String html) {
        this.html = html;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("numbered_list")
    public String getNumberedList() {
        return numberedList;
    }

    @JsonProperty("numbered_list")
    public void setNumberedList(String numberedList) {
        this.numberedList = numberedList;
    }

}