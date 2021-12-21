package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteTableau extends PublicationBodyItem {

    @JsonProperty("url_REQ")
    private String urlReq;
    @JsonProperty("placeholder_image_location")
    private String placeholderImageLocation;
    @JsonProperty("hidetabs")
    private String hidetabs;
    @JsonProperty("device")
    private String device;
    @JsonProperty("retry_intervals")
    private List<String> retryIntervals = null;

    public WebsiteTableau(@JsonProperty(value = "url_REQ", required = true)String urlReq) {
        this.urlReq = urlReq;
    }

    @JsonProperty("url_REQ")
    public String getUrlReq() {
        return urlReq;
    }

    @JsonProperty("url_REQ")
    public void setUrlReq(String urlReq) {
        this.urlReq = urlReq;
    }

    @JsonProperty("placeholder_image_location")
    public String getPlaceholderImageLocation() {
        return placeholderImageLocation;
    }

    @JsonProperty("placeholder_image_location")
    public void setPlaceholderImageLocation(String placeholderImageLocation) {
        this.placeholderImageLocation = placeholderImageLocation;
    }

    @JsonProperty("hidetabs")
    public String getHidetabs() {
        return hidetabs;
    }

    @JsonProperty("hidetabs")
    public void setHidetabs(String hidetabs) {
        this.hidetabs = hidetabs;
    }

    @JsonProperty("device")
    public String getDevice() {
        return device;
    }

    @JsonProperty("device")
    public void setDevice(String device) {
        this.device = device;
    }

    @JsonProperty("retry_intervals")
    public List<String> getRetryIntervals() {
        return retryIntervals;
    }

    @JsonProperty("retry_intervals")
    public void setRetryIntervals(List<String> retryIntervals) {
        this.retryIntervals = retryIntervals;
    }

}