package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteIconListItem extends PublicationBodyItem {

    @JsonProperty("heading")
    private String heading;

    @JsonProperty("item_link")
    private List<AbstractWebsiteItemlink> abstractWebsiteItemlink = null;

    @JsonProperty("description_REQ")
    private String descriptionReq;
    @JsonProperty("image_REQ")
    private String imageReq;

    @JsonProperty("heading")
    public String getHeading() {
        return heading;
    }

    @JsonProperty("heading")
    public void setHeading(String heading) {
        this.heading = heading;
    }

    @JsonProperty("item_link")
    public List<AbstractWebsiteItemlink> getItemlink() {
        return abstractWebsiteItemlink;
    }

    @JsonProperty("item_link")
    public void setItemlink(List<AbstractWebsiteItemlink> abstractWebsiteItemlink) {
        this.abstractWebsiteItemlink = abstractWebsiteItemlink;
    }

    @JsonProperty("description_REQ")
    public String getDescriptionReq() {
        return descriptionReq;
    }

    @JsonProperty("description_REQ")
    public void setDescriptionReq(String descriptionReq) {
        this.descriptionReq = descriptionReq;
    }

    @JsonProperty("image_REQ")
    public String getImageReq() {
        return imageReq;
    }

    @JsonProperty("image_REQ")
    public void setImageReq(String imageReq) {
        this.imageReq = imageReq;
    }

}