package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteGallerySection extends PublicationBodyItem {

    @JsonProperty("heading")
    private String heading;
    @JsonProperty("headinglevel")
    private String headinglevel;
    @JsonProperty("description")
    private String description;
    @JsonProperty("galleryitems")
    private List<WebsiteGalleryItem> galleryitems = null;

    @JsonProperty("heading")
    public String getHeading() {
        return heading;
    }

    @JsonProperty("heading")
    public void setHeading(String heading) {
        this.heading = heading;
    }

    @JsonProperty("headinglevel")
    public String getHeadinglevel() {
        return headinglevel;
    }

    @JsonProperty("headinglevel")
    public void setHeadinglevel(String headinglevel) {
        this.headinglevel = headinglevel;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("galleryitems")
    public List<WebsiteGalleryItem> getGalleryitems() {
        return galleryitems;
    }

    @JsonProperty("galleryitems")
    public void setGalleryitems(List<WebsiteGalleryItem> galleryitems) {
        this.galleryitems = galleryitems;
    }
}