package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.nhs.digital.arc.json.BasicBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("change_notice")
public class PublicationsystemChangeNotice extends BasicBodyItem {

    @JsonProperty("title")
    private String title;
    @JsonProperty("date_REQ")
    private String dateReq;
    @JsonProperty("content_REQ")
    private String contentReq;

    public PublicationsystemChangeNotice(@JsonProperty(value = "date_REQ", required = true) String dateReq,
                                         @JsonProperty(value = "content_REQ", required = true) String contentReq) {
        this.dateReq = dateReq;
        this.contentReq = contentReq;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("date_REQ")
    public String getDateReq() {
        return dateReq;
    }

    @JsonProperty("date_REQ")
    public void setDateReq(String dateReq) {
        this.dateReq = dateReq;
    }

    @JsonProperty("content_REQ")
    public String getContentReq() {
        return contentReq;
    }

    @JsonProperty("content_REQ")
    public void setContentReq(String contentReq) {
        this.contentReq = contentReq;
    }

}