package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteStatisticsFeedItem extends PublicationBodyItem {

    @JsonProperty("prefix_REQ")
    private String prefixReq;
    @JsonProperty("url_of_number_REQ")
    private String urlOfNumberReq;
    @JsonProperty("suffix_REQ")
    private String suffixReq;
    @JsonProperty("trend_REQ")
    private String trendReq;
    @JsonProperty("headline_description_REQ")
    private String headlineDescriptionReq;
    @JsonProperty("further_qualifying_information")
    private String furtherQualifyingInformation;

    public WebsiteStatisticsFeedItem(@JsonProperty(value = "prefix_REQ", required = true)String prefixReq,
                                     @JsonProperty(value = "url_of_number_REQ", required = true)String urlOfNumberReq,
                                     @JsonProperty(value = "suffix_REQ", required = true)String suffixReq,
                                     @JsonProperty(value = "trend_REQ", required = true)String trendReq,
                                     @JsonProperty(value = "headline_description_REQ", required = true)String headlineDescriptionReq) {
        this.prefixReq = prefixReq;
        this.urlOfNumberReq = urlOfNumberReq;
        this.suffixReq = suffixReq;
        this.trendReq = trendReq;
        this.headlineDescriptionReq = headlineDescriptionReq;
    }

    @JsonProperty("prefix_REQ")
    public String getPrefixReq() {
        return prefixReq;
    }

    @JsonProperty("prefix_REQ")
    public void setPrefixReq(String prefixReq) {
        this.prefixReq = prefixReq;
    }

    @JsonProperty("url_of_number_REQ")
    public String getUrlOfNumberReq() {
        return urlOfNumberReq;
    }

    @JsonProperty("url_of_number_REQ")
    public void setUrlOfNumberReq(String urlOfNumberReq) {
        this.urlOfNumberReq = urlOfNumberReq;
    }

    @JsonProperty("suffix_REQ")
    public String getSuffixReq() {
        return suffixReq;
    }

    @JsonProperty("suffix_REQ")
    public void setSuffixReq(String suffixReq) {
        this.suffixReq = suffixReq;
    }

    @JsonProperty("trend_REQ")
    public String getTrendReq() {
        return trendReq;
    }

    @JsonProperty("trend_REQ")
    public void setTrendReq(String trendReq) {
        this.trendReq = trendReq;
    }

    @JsonProperty("headline_description_REQ")
    public String getHeadlineDescriptionReq() {
        return headlineDescriptionReq;
    }

    @JsonProperty("headline_description_REQ")
    public void setHeadlineDescriptionReq(String headlineDescriptionReq) {
        this.headlineDescriptionReq = headlineDescriptionReq;
    }

    @JsonProperty("further_qualifying_information")
    public String getFurtherQualifyingInformation() {
        return furtherQualifyingInformation;
    }

    @JsonProperty("further_qualifying_information")
    public void setFurtherQualifyingInformation(String furtherQualifyingInformation) {
        this.furtherQualifyingInformation = furtherQualifyingInformation;
    }

}