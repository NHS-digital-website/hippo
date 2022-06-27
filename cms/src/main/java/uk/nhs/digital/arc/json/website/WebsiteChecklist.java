package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteChecklist extends PublicationBodyItem {

    @JsonProperty("heading")
    private String heading;
    @JsonProperty("icon_REQ")
    private String iconReq;
    @JsonProperty("custom_icon")
    private String customIcon;
    @JsonProperty("listentries_REQ")
    private List<String> listentriesReq = null;

    public WebsiteChecklist(@JsonProperty(value = "icon_REQ", required = true)String iconReq,
                            @JsonProperty(value = "listentries_REQ", required = true)List<String> listentriesReq) {
        this.iconReq = iconReq;
        this.listentriesReq = listentriesReq;
    }

    @JsonProperty("heading")
    public String getHeading() {
        return heading;
    }

    @JsonProperty("heading")
    public void setHeading(String heading) {
        this.heading = heading;
    }

    @JsonProperty("icon_REQ")
    public String getIconReq() {
        return iconReq;
    }

    @JsonProperty("icon_REQ")
    public void setIconReq(String iconReq) {
        this.iconReq = iconReq;
    }

    @JsonProperty("custom_icon")
    public String getCustomIcon() {
        return customIcon;
    }

    @JsonProperty("custom_icon")
    public void setCustomIcon(String customIcon) {
        this.customIcon = customIcon;
    }

    @JsonProperty("listentries_REQ")
    public List<String> getListentriesReq() {
        return listentriesReq;
    }

    @JsonProperty("listentries_REQ")
    public void setListentriesReq(List<String> listentriesReq) {
        this.listentriesReq = listentriesReq;
    }

}