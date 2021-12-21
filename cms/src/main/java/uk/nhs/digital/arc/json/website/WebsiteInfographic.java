package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteInfographic extends PublicationBodyItem {

    @JsonProperty("icon")
    private String icon;
    @JsonProperty("headline_REQ")
    private String headlineReq;
    @JsonProperty("explanatory_line")
    private String explanatoryLine;
    @JsonProperty("qualifying_information")
    private String qualifyingInformation;
    @JsonProperty("colour_REQ")
    private String colourReq;

    public WebsiteInfographic(@JsonProperty(value = "headline_REQ", required = true)String headlineReq,
                              @JsonProperty(value = "colour_REQ", required = true)String colourReq) {
        this.headlineReq = headlineReq;
        this.colourReq = colourReq;
    }

    @JsonProperty("icon")
    public String getIcon() {
        return icon;
    }

    @JsonProperty("icon")
    public void setIcon(String icon) {
        this.icon = icon;
    }

    @JsonProperty("headline_REQ")
    public String getHeadlineReq() {
        return headlineReq;
    }

    @JsonProperty("headline_REQ")
    public void setHeadlineReq(String headlineReq) {
        this.headlineReq = headlineReq;
    }

    @JsonProperty("explanatory_line")
    public String getExplanatoryLine() {
        return explanatoryLine;
    }

    @JsonProperty("explanatory_line")
    public void setExplanatoryLine(String explanatoryLine) {
        this.explanatoryLine = explanatoryLine;
    }

    @JsonProperty("qualifying_information")
    public String getQualifyingInformation() {
        return qualifyingInformation;
    }

    @JsonProperty("qualifying_information")
    public void setQualifyingInformation(String qualifyingInformation) {
        this.qualifyingInformation = qualifyingInformation;
    }

    @JsonProperty("colour_REQ")
    public String getColourReq() {
        return colourReq;
    }

    @JsonProperty("colour_REQ")
    public void setColourReq(String colourReq) {
        this.colourReq = colourReq;
    }

}