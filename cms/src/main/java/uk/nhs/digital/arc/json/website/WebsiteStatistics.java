package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteStatistics extends PublicationBodyItem {

    @JsonProperty("heading")
    private String heading;
    @JsonProperty("heading_level")
    private String headingLevel;
    @JsonProperty("colour_scheme_REQ")
    private String colourSchemeReq;
    @JsonProperty("animation_REQ")
    private String animationReq;
    @JsonProperty("modules_REQ")
    private List<WebsiteStatisticsFeedItem> modulesReq = null;

    public WebsiteStatistics(@JsonProperty(value = "colour_scheme_REQ", required = true)String colourSchemeReq,
                             @JsonProperty(value = "animation_REQ", required = true)String animationReq,
                             @JsonProperty(value = "modules_REQ", required = true)List<WebsiteStatisticsFeedItem> modulesReq) {
        this.colourSchemeReq = colourSchemeReq;
        this.animationReq = animationReq;
        this.modulesReq = modulesReq;
    }

    @JsonProperty("heading")
    public String getHeading() {
        return heading;
    }

    @JsonProperty("heading")
    public void setHeading(String heading) {
        this.heading = heading;
    }

    @JsonProperty("heading_level")
    public String getHeadingLevel() {
        return headingLevel;
    }

    @JsonProperty("heading_level")
    public void setHeadingLevel(String headingLevel) {
        this.headingLevel = headingLevel;
    }

    @JsonProperty("colour_sheme_REQ")
    public String getColourSchemeReq() {
        return colourSchemeReq;
    }

    @JsonProperty("colour_sheme_REQ")
    public void setColourSchemeReq(String colourSchemeReq) {
        this.colourSchemeReq = colourSchemeReq;
    }

    @JsonProperty("animation_REQ")
    public String getAnimationReq() {
        return animationReq;
    }

    @JsonProperty("animation_REQ")
    public void setAnimationReq(String animationReq) {
        this.animationReq = animationReq;
    }

    @JsonProperty("modules_REQ")
    public List<WebsiteStatisticsFeedItem> getModulesReq() {
        return modulesReq;
    }

    @JsonProperty("modules_REQ")
    public void setModulesReq(List<WebsiteStatisticsFeedItem> modulesReq) {
        this.modulesReq = modulesReq;
    }
}