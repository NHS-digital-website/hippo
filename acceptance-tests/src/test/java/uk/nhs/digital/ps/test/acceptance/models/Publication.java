package uk.nhs.digital.ps.test.acceptance.models;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Publication {

    private String name;
    private String title;
    private String summary;
    private GeographicCoverage geographicCoverage;
    private InformationType informationType;
    private Granularity granularity;
    private Instant nominalDate;
    boolean publiclyAccessible;

    private List<Attachment> attachments = new ArrayList<>();

    private final PublicationStatus status;
    private Taxonomy taxonomy;


    Publication(final PublicationBuilder builder) {
        name = builder.getName();
        title = builder.getTitle();
        summary = builder.getSummary();
        geographicCoverage = builder.getGeographicCoverage();
        informationType = builder.getInformationType();
        granularity = builder.getGranularity();
        nominalDate = builder.getNominalDate();
        taxonomy = builder.getTaxonomy();
        attachments = builder.getAttachments();
        publiclyAccessible = builder.isPubliclyAccessible();

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

    public String getNominalDateFormatted() {
        final int weeksUntilCutOff = 8;
        final int daysInAWeek = 7;
        final int daysUntilCutOff = weeksUntilCutOff * daysInAWeek;

        String pattern = nominalDate.isAfter(Instant.now().plus(daysUntilCutOff, ChronoUnit.DAYS))
            ? "MMM yyyy"
            : "d MMM yyyy";

        return new SimpleDateFormat(pattern).format(new Date(nominalDate.toEpochMilli()));
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

    public boolean isPubliclyAccessible() {
        return publiclyAccessible;
    }


}
