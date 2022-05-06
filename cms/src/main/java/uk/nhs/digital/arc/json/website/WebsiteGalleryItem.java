package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteGalleryItem extends PublicationBodyItem {

    @JsonProperty("title")
    private String title;
    @JsonProperty("image_REQ")
    private String imageReq;
    @JsonProperty("imagealt_REQ")
    private String imagealtReq;
    @JsonProperty("imagewarning")
    private String imagewarning;
    @JsonProperty("description")
    private String description;
    @JsonProperty("relatedfiles")
    private List<WebsiteAssetlink> relatedfiles = null;

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("image_REQ")
    public String getImageReq() {
        return imageReq;
    }

    @JsonProperty("image_REQ")
    public void setImageReq(String imageReq) {
        this.imageReq = imageReq;
    }

    @JsonProperty("imagealt_REQ")
    public String getImagealtReq() {
        return imagealtReq;
    }

    @JsonProperty("imagealt_REQ")
    public void setImagealtReq(String imagealtReq) {
        this.imagealtReq = imagealtReq;
    }

    @JsonProperty("imagewarning")
    public String getImagewarning() {
        return imagewarning;
    }

    @JsonProperty("imagewarning")
    public void setImagewarning(String imagewarning) {
        this.imagewarning = imagewarning;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("relatedfiles")
    public List<WebsiteAssetlink> getRelatedfiles() {
        return relatedfiles;
    }

    @JsonProperty("relatedfiles")
    public void setRelatedfiles(List<WebsiteAssetlink> relatedfiles) {
        this.relatedfiles = relatedfiles;
    }
}