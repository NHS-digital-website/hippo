package uk.nhs.digital.ps.test.acceptance.models;

import java.util.List;

public class Publication {

    private String publicationName;
    private String publicationTitle;
    private String publicationSummary;
    private String geographicCoverage;
    private String informationType;
    private String granularity;
    private List<Attachment> attachments;
    private Taxonomy taxonomy;


    Publication(final PublicationBuilder builder) {
        this.publicationName = builder.getPublicationName();
        this.publicationTitle = builder.getPublicationTitle();
        this.publicationSummary = builder.getPublicationSummary();
        this.geographicCoverage = builder.getGeographicCoverage();
        this.informationType = builder.getInformationType();
        this.granularity = builder.getGranularity();
        this.attachments = builder.getAttachments();
        this.taxonomy = builder.getTaxonomy();
    }

    public String getPublicationName() {
        return publicationName;
    }

    public String getPublicationTitle() {
        return publicationTitle;
    }

    public String getPublicationSummary() {
        return publicationSummary;
    }

    public String getGeographicCoverage() {
        return geographicCoverage;
    }

    public String getInformationType() {
        return informationType;
    }

    public String getGranularity() {
        return granularity;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }

    public String getPublicationUrlName() {
        return publicationName;
    }
}
