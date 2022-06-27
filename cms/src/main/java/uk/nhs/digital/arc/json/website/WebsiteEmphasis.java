package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteEmphasis extends PublicationBodyItem {

    @JsonProperty("emphasis_type_REQ")
    private String emphasisTypeReq;
    @JsonProperty("heading")
    private String heading;
    @JsonProperty("body_REQ")
    private String bodyReq;
    @JsonProperty("image")
    private String image;

    public WebsiteEmphasis(@JsonProperty(value = "body_REQ", required = true)String bodyReq,
                              @JsonProperty(value = "emphasis_type_REQ", required = true)String emphasisTypeReq) {
        this.bodyReq = bodyReq;
        this.emphasisTypeReq = emphasisTypeReq;
    }

    @JsonProperty("heading")
    public String getHeading() {
        return heading;
    }

    @JsonProperty("heading")
    public void setHeading(String heading) {
        this.heading = heading;
    }

    @JsonProperty("body")
    public String getBodyReq() {
        return bodyReq;
    }

    @JsonProperty("content")
    public void setBodyReq(String bodyReq) {
        this.bodyReq = bodyReq;
    }

    @JsonProperty("emphasis_type_REQ")
    public String getEmphasisTypeReq() {
        return emphasisTypeReq;
    }

    @JsonProperty("emphasis_type_REQ")
    public void setEmphasisTypeReq(String emphasisTypeReq) {
        this.emphasisTypeReq = emphasisTypeReq;
    }

    @JsonProperty("image")
    public String getImage() {
        return image;
    }

    @JsonProperty("image")
    public void setImage(String image) {
        this.image = image;
    }
}