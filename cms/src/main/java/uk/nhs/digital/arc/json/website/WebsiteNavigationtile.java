package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteNavigationtile extends PublicationBodyItem {

    @JsonProperty("image_or_icon")
    private String imageOrIcon;
    @JsonProperty("description")
    private String description;
    @JsonProperty("action_description_REQ")
    private String actionDescriptionReq;
    @JsonProperty("items_REQ")
    private List<AbstractWebsiteItemlink> itemsReq = null;

    public WebsiteNavigationtile(@JsonProperty(value = "action_description_REQ", required = true)String actiondescriptionReq,
                                 @JsonProperty(value = "items_REQ", required = true)List<AbstractWebsiteItemlink> itemsReq) {
        this.actionDescriptionReq = actiondescriptionReq;
        this.itemsReq = itemsReq;
    }

    @JsonProperty("image_or_icon")
    public String getImageOrIcon() {
        return imageOrIcon;
    }

    @JsonProperty("image_or_icon")
    public void setImageOrIcon(String imageOrIcon) {
        this.imageOrIcon = imageOrIcon;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("action_description_REQ")
    public String getActionDescriptionReq() {
        return actionDescriptionReq;
    }

    @JsonProperty("action_description_REQ")
    public void setActionDescriptionReq(String actionDescriptionReq) {
        this.actionDescriptionReq = actionDescriptionReq;
    }

    @JsonProperty("items_REQ")
    public List<AbstractWebsiteItemlink> getItemsReq() {
        return itemsReq;
    }

    @JsonProperty("items_REQ")
    public void setItemsReq(List<AbstractWebsiteItemlink> itemsReq) {
        this.itemsReq = itemsReq;
    }

}