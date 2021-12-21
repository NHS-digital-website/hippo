package uk.nhs.digital.arc.json.website;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import uk.nhs.digital.arc.json.PublicationBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WebsiteNavigation extends PublicationBodyItem {

    @JsonProperty("optional_heading")
    private String optionalHeading;
    @JsonProperty("heading_type_REQ")
    private String headingTypeReq;
    @JsonProperty("section_introduction")
    private String sectionIntroduction;
    @JsonProperty("image_type_REQ")
    private String imageTypeReq;
    @JsonProperty("column_alignment_REQ")
    private String columnAlignmentReq;
    @JsonProperty("navigation_tiles_REQ")
    private List<WebsiteNavigationtile> navigationTilesReq = null;

    public WebsiteNavigation(@JsonProperty(value = "heading_type_REQ", required = true)String headingTypeReq,
                             @JsonProperty(value = "image_type_REQ", required = true)String imageTypeReq,
                             @JsonProperty(value = "column_alignment_REQ", required = true)String columnAlignmentReq,
                             @JsonProperty(value = "navigation_tiles_REQ", required = true)List<WebsiteNavigationtile> navigationTilesReq) {
        this.headingTypeReq = headingTypeReq;
        this.imageTypeReq = imageTypeReq;
        this.columnAlignmentReq = columnAlignmentReq;
        this.navigationTilesReq = navigationTilesReq;
    }

    @JsonProperty("optional_heading")
    public String getOptionalHeading() {
        return optionalHeading;
    }

    @JsonProperty("optional_heading")
    public void setOptionalHeading(String optionalHeading) {
        this.optionalHeading = optionalHeading;
    }

    @JsonProperty("heading_type_REQ")
    public String getHeadingTypeReq() {
        return headingTypeReq;
    }

    @JsonProperty("heading_type_REQ")
    public void setHeadingTypeReq(String headingTypeReq) {
        this.headingTypeReq = headingTypeReq;
    }

    @JsonProperty("section_introduction")
    public String getSectionIntroduction() {
        return sectionIntroduction;
    }

    @JsonProperty("section_introduction")
    public void setSectionIntroduction(String sectionIntroduction) {
        this.sectionIntroduction = sectionIntroduction;
    }

    @JsonProperty("image_type_REQ")
    public String getImageTypeReq() {
        return imageTypeReq;
    }

    @JsonProperty("image_type_REQ")
    public void setImageTypeReq(String imageTypeReq) {
        this.imageTypeReq = imageTypeReq;
    }

    @JsonProperty("column_alignment_REQ")
    public String getColumnAlignmentReq() {
        return columnAlignmentReq;
    }

    @JsonProperty("column_alignment_REQ")
    public void setColumnAlignmentReq(String columnAlignmentReq) {
        this.columnAlignmentReq = columnAlignmentReq;
    }

    @JsonProperty("navigation_tiles_REQ")
    public List<WebsiteNavigationtile> getNavigationTilesReq() {
        return navigationTilesReq;
    }

    @JsonProperty("navigation_tiles_REQ")
    public void setNavigationTilesReq(List<WebsiteNavigationtile> navigationTilesReq) {
        this.navigationTilesReq = navigationTilesReq;
    }

}