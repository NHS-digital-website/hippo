package uk.nhs.digital.arc.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemExternalattachment;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemReleaseSubject;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemResourceOrExternalLink;
import uk.nhs.digital.arc.json.publicationsystem.PublicationsystemSeriesreplaces;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Series extends ArcSeriesArchiveCommonDoc {

    @JsonProperty("summary_REQ")
    private String summaryReq;
    @JsonProperty("show_latest")
    private String showLatest;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonProperty("external_attachments")
    private List<PublicationsystemExternalattachment> externalAttachments = null;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonProperty("resource_links")
    private List<PublicationsystemResourceOrExternalLink> resourceLinks = null;

    @JsonProperty("statistician")
    private String statistician;
    @JsonProperty("team")
    private String team;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonProperty("series_replaces")
    private PublicationsystemSeriesreplaces seriesReplaces;
    @JsonProperty("short_title_REQ")
    private String shortTitleReq;
    @JsonProperty("sub_title")
    private String subTitle;
    @JsonProperty("date_naming_REQ")
    private String dateNamingReq;
    @JsonProperty("about")
    private String about;
    @JsonProperty("methodology")
    private String methodology;
    @JsonProperty("ref_number")
    private String refNumber;
    @JsonProperty("frequency")
    private String frequency;
    @JsonProperty("issn")
    private String issn;
    @JsonProperty("publication_tier_REQ")
    private String publicationTierReq;

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    @JsonProperty("release_subject")
    private List<PublicationsystemReleaseSubject> releaseSubject = null;

    public Series(@JsonProperty(value = "doctype_REQ", required = true)String doctypeReq,
                  @JsonProperty(value = "title_REQ", required = true)String titleReq,
                  @JsonProperty(value = "summary_REQ", required = true)String summaryReq,
                  @JsonProperty(value = "short_title_REQ", required = true)String shortTitleReq,
                  @JsonProperty(value = "date_naming_REQ", required = true)String dateNamingReq,
                  @JsonProperty(value = "publication_tier_REQ", required = true)String publicationTierReq) {
        this.doctypeReq = doctypeReq;
        this.titleReq = titleReq;
        this.summaryReq = summaryReq;
        this.shortTitleReq = shortTitleReq;
        this.dateNamingReq = dateNamingReq;
        this.publicationTierReq = publicationTierReq;
    }

    @JsonProperty("summary_REQ")
    public String getSummaryReq() {
        return summaryReq;
    }

    @JsonProperty("summary_REQ")
    public void setSummaryReq(String summaryReq) {
        this.summaryReq = summaryReq;
    }

    @JsonProperty("show_latest")
    public String getShowLatest() {
        return showLatest;
    }

    @JsonProperty("show_latest")
    public void setShowLatest(String showLatest) {
        this.showLatest = showLatest;
    }

    @JsonProperty("external_attachments")
    public List<PublicationsystemExternalattachment> getExternalAttachments() {
        return externalAttachments;
    }

    @JsonProperty("external_attachments")
    public void setExternalAttachments(List<PublicationsystemExternalattachment> externalAttachments) {
        this.externalAttachments = externalAttachments;
    }

    @JsonProperty("resource_links")
    public List<PublicationsystemResourceOrExternalLink> getResourceLinks() {
        return resourceLinks;
    }

    @JsonProperty("resource_links")
    public void setResourceLinks(List<PublicationsystemResourceOrExternalLink> resourceLinks) {
        this.resourceLinks = resourceLinks;
    }

    @JsonProperty("statistician")
    public String getStatistician() {
        return statistician;
    }

    @JsonProperty("statistician")
    public void setStatistician(String statistician) {
        this.statistician = statistician;
    }

    @JsonProperty("team")
    public String getTeam() {
        return team;
    }

    @JsonProperty("team")
    public void setTeam(String team) {
        this.team = team;
    }

    @JsonProperty("series_replaces")
    public PublicationsystemSeriesreplaces getSeriesReplaces() {
        return seriesReplaces;
    }

    @JsonProperty("series_replaces")
    public void setSeriesReplaces(PublicationsystemSeriesreplaces seriesReplaces) {
        this.seriesReplaces = seriesReplaces;
    }

    @JsonProperty("short_title_REQ")
    public String getShortTitleReq() {
        return shortTitleReq;
    }

    @JsonProperty("short_title_REQ")
    public void setShortTitleReq(String shortTitleReq) {
        this.shortTitleReq = shortTitleReq;
    }

    @JsonProperty("sub_title")
    public String getSubTitle() {
        return subTitle;
    }

    @JsonProperty("sub_title")
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    @JsonProperty("date_naming_REQ")
    public String getDateNamingReq() {
        return dateNamingReq;
    }

    @JsonProperty("date_naming_REQ")
    public void setDateNamingReq(String dateNamingReq) {
        this.dateNamingReq = dateNamingReq;
    }

    @JsonProperty("about")
    public String getAbout() {
        return about;
    }

    @JsonProperty("about")
    public void setAbout(String about) {
        this.about = about;
    }

    @JsonProperty("methodology")
    public String getMethodology() {
        return methodology;
    }

    @JsonProperty("methodology")
    public void setMethodology(String methodology) {
        this.methodology = methodology;
    }

    @JsonProperty("ref_number")
    public String getRefNumber() {
        return refNumber;
    }

    @JsonProperty("ref_number")
    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    @JsonProperty("frequency")
    public String getFrequency() {
        return frequency;
    }

    @JsonProperty("frequency")
    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    @JsonProperty("issn")
    public String getIssn() {
        return issn;
    }

    @JsonProperty("issn")
    public void setIssn(String issn) {
        this.issn = issn;
    }

    @JsonProperty("publication_tier_REQ")
    public String getPublicationTierReq() {
        return publicationTierReq;
    }

    @JsonProperty("publication_tier_REQ")
    public void setPublicationTierReq(String publicationTierReq) {
        this.publicationTierReq = publicationTierReq;
    }

    @JsonProperty("release_subject")
    public List<PublicationsystemReleaseSubject> getReleaseSubject() {
        return releaseSubject;
    }

    @JsonProperty("release_subject")
    public void setReleaseSubject(List<PublicationsystemReleaseSubject> releaseSubject) {
        this.releaseSubject = releaseSubject;
    }
}