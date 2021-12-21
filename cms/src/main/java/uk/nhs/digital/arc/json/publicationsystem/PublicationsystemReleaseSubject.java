package uk.nhs.digital.arc.json.publicationsystem;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import uk.nhs.digital.arc.json.BasicBodyItem;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeName("release_subject")
public class PublicationsystemReleaseSubject extends BasicBodyItem {

    @JsonProperty("section_type")
    private String sectionType;
    @JsonProperty("organisation")
    private String organisation;
    @JsonProperty("recipients")
    private List<String> recipients = null;
    @JsonProperty("additional_detail")
    private String additionalDetail;

    @JsonProperty("section_type")
    public String getSectionType() {
        return sectionType;
    }

    @JsonProperty("section_type")
    public void setSectionType(String sectionType) {
        this.sectionType = sectionType;
    }

    @JsonProperty("organisation")
    public String getOrganisation() {
        return organisation;
    }

    @JsonProperty("organisation")
    public void setOrganisation(String organisation) {
        this.organisation = organisation;
    }

    @JsonProperty("recipients")
    public List<String> getRecipients() {
        return recipients;
    }

    @JsonProperty("recipients")
    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    @JsonProperty("additional_detail")
    public String getAdditionalDetail() {
        return additionalDetail;
    }

    @JsonProperty("additional_detail")
    public void setAdditionalDetail(String additionalDetail) {
        this.additionalDetail = additionalDetail;
    }

}