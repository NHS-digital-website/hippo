package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicationsystemImagesection extends PublicationBodyItem {

    @JsonProperty("image_REQ")
    private String imageReq;

    @JsonProperty("imageSize_REQ")
    private String imageSizeReq;

    @JsonProperty("altText_REQ")
    private String altTextReq;

    @JsonProperty("link")
    private String link;

    @JsonProperty("caption")
    private String caption;

    public PublicationsystemImagesection(@JsonProperty(value = "image_REQ", required = true) String imageReq,
                                         @JsonProperty(value = "image_size_REQ", required = true) String imageSizeReq,
                                         @JsonProperty(value = "alt_text_REQ", required = true) String altTextReq,
                                         @JsonProperty(value = "link") String link,
                                         @JsonProperty(value = "caption") String caption) {
        super();
        this.imageReq = imageReq;
        this.imageSizeReq = imageSizeReq;
        this.altTextReq = altTextReq;
        this.link = link;
        this.caption = caption;
    }

    @JsonProperty("image_REQ")
    public String getImageReq() {
        return imageReq;
    }

    @JsonProperty("image_REQ")
    public void setImageReq(String imageReq) {
        this.imageReq = imageReq;
    }

    @JsonProperty("image_size_REQ")
    public String getImageSizeReq() {
        return imageSizeReq;
    }

    @JsonProperty("image_size_REQ")
    public void setImageSizeReq(String imageSizeReq) {
        this.imageSizeReq = imageSizeReq;
    }

    @JsonProperty("alt_text_REQ")
    public String getAltTextReq() {
        return altTextReq;
    }

    @JsonProperty("alt_text_REQ")
    public void setAltTextReq(String altTextReq) {
        this.altTextReq = altTextReq;
    }

    @JsonProperty("link")
    public String getLink() {
        return link;
    }

    @JsonProperty("link")
    public void setLink(String link) {
        this.link = link;
    }

    @JsonProperty("caption")
    public String getCaption() {
        return caption;
    }

    @JsonProperty("caption")
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public List<String> getAllReferencedExternalUrls() {
        if (getImageReq() == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(Arrays.asList(new String[]{getImageReq()}));
        }
    }
}
