package uk.nhs.digital.ps.test.acceptance.models;

import static uk.nhs.digital.ps.test.acceptance.util.FormatHelper.formatInstant;

import uk.nhs.digital.ps.test.acceptance.models.section.BodySection;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Publication {

    private String name;
    private String title;
    private String summary;
    private GeographicCoverage geographicCoverage;
    private InformationType informationType;
    private Granularity granularity;
    private NominalPublicationDate nominalPublicationDate;
    private boolean publiclyAccessible;
    private List<Page> pages;
    private List<BodySection> keyFactImages;

    private List<Attachment> attachments;

    private final PublicationState state;
    private Taxonomy taxonomy;


    Publication(final PublicationBuilder builder) {
        name = builder.getName();
        title = builder.getTitle();
        summary = builder.getSummary();
        geographicCoverage = builder.getGeographicCoverage();
        informationType = builder.getInformationType();
        granularity = builder.getGranularity();
        nominalPublicationDate = new NominalPublicationDate(builder.getNominalDate());
        taxonomy = builder.getTaxonomy();
        attachments = builder.getAttachments();
        publiclyAccessible = builder.isPubliclyAccessible();
        pages = builder.getPages();
        keyFactImages = builder.getKeyFactImages();

        state = builder.getState();
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

    public NominalPublicationDate getNominalPublicationDate() {
        return nominalPublicationDate;
    }

    public List<Attachment> getAttachments() {
        return new ArrayList<>(attachments);
    }

    public Taxonomy getTaxonomy() {
        return taxonomy;
    }

    public List<Page> getPages() {
        return pages;
    }

    public String getPublicationUrlName() {
        return name.toLowerCase().replace(' ', '-');
    }

    public PublicationState getState() {
        return state;
    }

    public boolean isPubliclyAccessible() {
        return publiclyAccessible;
    }

    public String getSummaryTruncated() {
        return truncate(getSummary(), 100);
    }

    public List<BodySection> getKeyFactImages() {
        return keyFactImages;
    }

    public static class NominalPublicationDate implements Comparable<NominalPublicationDate> {

        static final String RESTRICTED_NOMINAL_DATE_PATTERN = "MMM yyyy";
        static final String FULL_NOMINAL_DATE_PATTERN = "d MMM yyyy";

        private final Instant instant;

        private NominalPublicationDate(final Instant instant) {
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

        public String inFormat(final String datePattern) {
            return formatInstant(instant, datePattern);
        }

        public Instant asInstant() {
            return instant;
        }

        @Override
        public int compareTo(final NominalPublicationDate other) {
            return asInstant().compareTo(other.asInstant());
        }
    }

    private String truncate(String text, final int size) {

        int endIndex = Math.max(size, text.indexOf(" ", size));

        if (endIndex < text.length()) {
            return text.substring(0, endIndex) + "...";
        } else {
            return text;
        }
    }
}
