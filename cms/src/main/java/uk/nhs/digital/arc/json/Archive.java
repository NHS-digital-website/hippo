package uk.nhs.digital.arc.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Archive extends ArcSeriesArchiveCommonDoc {

    @JsonProperty("Summary_REQ")
    private String summaryReq;

    public Archive(@JsonProperty(value = "doctype_REQ", required = true) String doctypeReq,
                   @JsonProperty(value = "title_REQ", required = true) String titleReq,
                   @JsonProperty(value = "summary_REQ", required = true) String summaryReq) {
        this.doctypeReq = doctypeReq;
        this.titleReq = titleReq;
        this.summaryReq = summaryReq;
    }

    @JsonProperty("Summary_REQ")
    public String getSummaryReq() {
        return summaryReq;
    }

    @JsonProperty("Summary_REQ")
    public void setSummaryReq(String summaryReq) {
        this.summaryReq = summaryReq;
    }
}