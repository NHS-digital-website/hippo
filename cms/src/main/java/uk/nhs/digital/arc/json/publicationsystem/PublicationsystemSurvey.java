package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.nhs.digital.arc.json.PublicationBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("publication_survey")
public class PublicationsystemSurvey extends PublicationBodyItem {

    @JsonProperty("date_REQ")
    private String dateReq;
    @JsonProperty("link_REQ")
    private String linkReq;

    public PublicationsystemSurvey(@JsonProperty(value = "date_REQ", required = true) String dateReq,
                                   @JsonProperty(value = "link_REQ", required = true) String linkReq) {
        this.dateReq = dateReq;
        this.linkReq = linkReq;
    }

    @JsonProperty("date_REQ")
    public String getDateReq() {
        return dateReq;
    }

    @JsonProperty("date_REQ")
    public void setDateReq(String dateReq) {
        this.dateReq = dateReq;
    }

    @JsonProperty("link_REQ")
    public String getLinkReq() {
        return linkReq;
    }

    @JsonProperty("link_REQ")
    public void setLinkReq(String linkReq) {
        this.linkReq = linkReq;
    }
}