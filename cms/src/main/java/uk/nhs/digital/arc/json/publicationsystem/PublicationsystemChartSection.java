package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PublicationsystemChartSection extends PublicationBodyItem {

    @JsonProperty("type_REQ")
    private String typeReq;
    @JsonProperty("title_REQ")
    private String titleReq;
    @JsonProperty("data_file_REQ")
    private String dataFileReq;
    @JsonProperty("yTitle_REQ")
    private String yTitleReq;

    public PublicationsystemChartSection(@JsonProperty(value = "type_REQ", required = true) String typeReq,
                                         @JsonProperty(value = "title_REQ", required = true) String titleReq,
                                         @JsonProperty(value = "data_file_REQ", required = true) String dataFileReq,
                                         @JsonProperty(value = "yTitle_REQ", required = true) String yTitleReq) {
        this.typeReq = typeReq;
        this.titleReq = titleReq;
        this.dataFileReq = dataFileReq;
        this.yTitleReq = yTitleReq;
    }


    @JsonProperty("type_REQ")
    public String getTypeReq() {
        return typeReq;
    }

    @JsonProperty("type_REQ")
    public void setTypeReq(String typeReq) {
        this.typeReq = typeReq;
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

    @JsonProperty("yTitle_REQ")
    public String getyTitleReq() {
        return yTitleReq;
    }

    @JsonProperty("yTitle_REQ")
    public void setyTitleReq(String yTitleReq) {
        this.yTitleReq = yTitleReq;
    }

    @Override
    public List<String> getAllReferencedExternalUrls() {
        if (getDataFileReq() == null) {
            return new ArrayList<>();
        } else {
            return new ArrayList<>(Arrays.asList(new String[]{getDataFileReq()}));
        }
    }
}