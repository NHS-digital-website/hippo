package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteCtabutton extends PublicationBodyItem {

    @JsonProperty("title_REQ")
    private String titleReq;
    @JsonProperty("button_type_REQ")
    private String buttontypeReq;
    @JsonProperty("items")
    private List<AbstractWebsiteItemlink> items = null;
    @JsonProperty("alignment_REQ")
    private String alignmentReq;

    public WebsiteCtabutton(@JsonProperty(value = "title_REQ", required = true) String titleReq,
                            @JsonProperty(value = "button_type_REQ", required = true) String buttontypeReq,
                            @JsonProperty(value = "alignment_REQ", required = true) String alignmentReq) {
        this.titleReq = titleReq;
        this.buttontypeReq = buttontypeReq;
        this.alignmentReq = alignmentReq;
    }

    @JsonProperty("title_REQ")
    public String getTitleReq() {
        return titleReq;
    }

    @JsonProperty("title_REQ")
    public void setTitleReq(String titleReq) {
        this.titleReq = titleReq;
    }

    @JsonProperty("buttontype_REQ")
    public String getButtontypeReq() {
        return buttontypeReq;
    }

    @JsonProperty("buttontype_REQ")
    public void setButtontypeReq(String buttontypeReq) {
        this.buttontypeReq = buttontypeReq;
    }

    @JsonProperty("items")
    public List<AbstractWebsiteItemlink> getItems() {
        return items;
    }

    @JsonProperty("items")
    public void setItems(List<AbstractWebsiteItemlink> items) {
        this.items = items;
    }

    @JsonProperty("alignmentReq")
    public String getAlignmentReq() {
        return alignmentReq;
    }

    @JsonProperty("alignmentReq")
    public void setAlignmentReq(String alignmentReq) {
        this.alignmentReq = alignmentReq;
    }
}