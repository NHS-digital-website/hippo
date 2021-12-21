package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteDynamicChartSection extends PublicationBodyItem {

    @JsonProperty("type")
    private String type;
    @JsonProperty("title_REQ")
    private String titleReq;
    @JsonProperty("url_REQ")
    private String urlReq;
    @JsonProperty("yTitle")
    private String yTitle;
    @JsonProperty("xTitle")
    private String xTitle;

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("title_REQ")
    public String getTitleReq() {
        return titleReq;
    }

    @JsonProperty("title_REQ")
    public void setTitleReq(String titleReq) {
        this.titleReq = titleReq;
    }

    @JsonProperty("url_REQ")
    public String getUrlReq() {
        return urlReq;
    }

    @JsonProperty("url_REQ")
    public void setUrlReq(String urlReq) {
        this.urlReq = urlReq;
    }

    @JsonProperty("yTitle")
    public String getYTitle() {
        return yTitle;
    }

    @JsonProperty("yTitle")
    public void setYTitle(String yTitle) {
        this.yTitle = yTitle;
    }

    @JsonProperty("xTitle")
    public String getXTitle() {
        return xTitle;
    }

    @JsonProperty("xTitle")
    public void setXTitle(String xTitle) {
        this.xTitle = xTitle;
    }

}