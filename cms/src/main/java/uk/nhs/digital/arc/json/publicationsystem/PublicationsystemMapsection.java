package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicationsystemMapsection extends PublicationBodyItem {

    @JsonProperty("mapSource_REQ")
    private String mapSourceReq;
    @JsonProperty("title_REQ")
    private String titleReq;
    @JsonProperty("data_file_REQ")
    private String dataFileReq;


    public PublicationsystemMapsection(@JsonProperty(value = "title_REQ", required = true) String titleReq,
                                            @JsonProperty(value = "mapSource_REQ", required = true) String mapSourceReq,
                                            @JsonProperty(value = "data_file_REQ", required = true) String dataFileReq) {
        this.titleReq = titleReq;
        this.mapSourceReq = mapSourceReq;
        this.dataFileReq = dataFileReq;
    }


    @JsonProperty("mapSource_REQ")
    public String getMapSourceReq() {
        return mapSourceReq;
    }

    @JsonProperty("mapSource_REQ")
    public void setMapSourceReq(String mapSourceReq) {
        this.mapSourceReq = mapSourceReq;
    }

    @JsonProperty("title_REQ")
    public String getTitleReq() {
        return titleReq;
    }

    @JsonProperty("title_REQ")
    public void setTitleReq(String titleReq) {
        this.titleReq = titleReq;
    }

    @JsonProperty("data_file_REQ")
    public String getDataFileReq() {
        return dataFileReq;
    }

    @JsonProperty("data_file_REQ")
    public void setDataFileReq(String dataFileReq) {
        this.dataFileReq = dataFileReq;
    }

}