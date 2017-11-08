package uk.nhs.digital.ps.test.acceptance.models;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static uk.nhs.digital.ps.test.acceptance.util.FormatHelper.formatInstant;

public class Publication {

    private String name;
    private String title;
    private String summary;
    private GeographicCoverage geographicCoverage;
    private InformationType informationType;
    private Granularity granularity;
    private NominalPublicationDate nominalPublicationDateDate;
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
        nominalPublicationDateDate = new NominalPublicationDate(builder.getNominalDate());
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

    public NominalPublicationDate getNominalPublicationDateDate() {
        return nominalPublicationDateDate;
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

    public static class NominalPublicationDate {

        static final String RESTRICTED_NOMINAL_DATE_PATTERN = "MMM yyyy";
        static final String FULL_NOMINAL_DATE_PATTERN = "d MMM yyyy";

        private final Instant instant;

        NominalPublicationDate(final Instant instant) {
            this.instant = instant;
        }

        public String formattedInRespectToCutOff() {

            final int weeksUntilCutOff = 8;
            final int daysInAWeek = 7;
            final int daysUntilCutOff = weeksUntilCutOff * daysInAWeek;

            return instant.isAfter(Instant.now().plus(daysUntilCutOff, ChronoUnit.DAYS))
                ? inRestrictedFormat()
                : inFullFormat();
        }

        public String inRestrictedFormat() {
            return formatInstant(instant, RESTRICTED_NOMINAL_DATE_PATTERN);
        }

        public String inFullFormat() {
            return formatInstant(instant, FULL_NOMINAL_DATE_PATTERN);
        }

        public Instant asInstant() {
            return instant;
        }
    }
}
