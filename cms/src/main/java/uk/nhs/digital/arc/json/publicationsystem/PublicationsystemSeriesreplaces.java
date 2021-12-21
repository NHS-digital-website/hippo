package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.nhs.digital.arc.json.BasicBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("series_replaces")
public class PublicationsystemSeriesreplaces extends BasicBodyItem {

    @JsonProperty("date_REQ")
    private String dateReq;
    @JsonProperty("mirror")
    private String mirror;
    @JsonProperty("why_replaced")
    private String whyReplaced;

    public PublicationsystemSeriesreplaces(@JsonProperty(value = "date_REQ", required = true)String dateReq) {
        this.dateReq = dateReq;
    }

    @JsonProperty("date_REQ")
    public String getDateReq() {
        return dateReq;
    }

    @JsonProperty("date_REQ")
    public void setDateReq(String dateReq) {
        this.dateReq = dateReq;
    }

    @JsonProperty("mirror")
    public String getMirror() {
        return mirror;
    }

    @JsonProperty("mirror")
    public void setMirror(String mirror) {
        this.mirror = mirror;
    }

    @JsonProperty("why_replaced")
    public String getWhyReplaced() {
        return whyReplaced;
    }

    @JsonProperty("why_replaced")
    public void setWhyReplaced(String whyReplaced) {
        this.whyReplaced = whyReplaced;
    }


}