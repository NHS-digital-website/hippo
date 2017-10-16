package uk.nhs.digital.ps.test.acceptance.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Publication {

    private String name;
    private String title;
    private String summary;
    private GeographicCoverage geographicCoverage;
    private InformationType informationType;
    private Granularity granularity;
    private Instant nominalDate;

    private List<Attachment> attachments = new ArrayList<>();

    private final PublicationStatus status;
    private Taxonomy taxonomy;


    Publication(final PublicationBuilder builder) {
        name = builder.getPublicationName();
        title = builder.getPublicationTitle();
        summary = builder.getPublicationSummary();
        geographicCoverage = builder.getGeographicCoverage();
        informationType = builder.getInformationType();
        granularity = builder.getGranularity();
        nominalDate = builder.getNominalDate();
        taxonomy = builder.getTaxonomy();
        attachments = builder.getAttachments();

        status = builder.getStatus();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public GeographicCoverage getGeographicCoverage() {
        return geographicCoverage;
    }

    public InformationType getInformationType() {
        return informationType;
    }

    public Granularity getGranularity() {
        return granularity;
    }

    public Instant getNominalDate() {
        return nominalDate;
    }

    public List<Attachment> getAttachments() {
        return new ArrayList<>(attachments);
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }

    public String getPublicationUrlName() {
        return name.toLowerCase().replace(' ', '-');
    }

    public PublicationStatus getStatus() {
        return status;
    }
}
