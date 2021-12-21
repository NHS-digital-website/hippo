package uk.nhs.digital.arc.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ArcDoctype extends ReferencableUrlElement {

    @JsonProperty("doctype_REQ")
    protected String doctypeReq;

    @JsonProperty("title_REQ")
    protected String titleReq;

    @JsonProperty("title_REQ")
    public String getTitleReq() {
        return titleReq;
    }

    @JsonProperty("title_REQ")
    public void setTitleReq(String titleReq) {
        this.titleReq = titleReq;
    }

    @JsonProperty("doctype_REQ")
    public String getDoctypeReq() {
        return doctypeReq;
    }

    @JsonProperty("doctype_REQ")
    public void setDoctypeReq(String doctypeReq) {
        this.doctypeReq = doctypeReq;
    }
}
