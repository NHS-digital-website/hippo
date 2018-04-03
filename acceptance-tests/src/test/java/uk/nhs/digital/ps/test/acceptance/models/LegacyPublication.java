package uk.nhs.digital.ps.test.acceptance.models;

import static uk.nhs.digital.ps.test.acceptance.util.FormatHelper.formatInstant;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LegacyPublication {

    private String name;
    private String title;
    private NominalPublicationDate nominalPublicationDate;
    private boolean publiclyAccessible;

    private List<Attachment> attachments = new ArrayList<>();

    private final PublicationState state;

    LegacyPublication(final LegacyPublicationBuilder builder) {
        name = builder.getName();
        title = builder.getTitle();
        nominalPublicationDate = new NominalPublicationDate(builder.getNominalDate());
        attachments = builder.getAttachments();
        publiclyAccessible = builder.isPubliclyAccessible();

        state = builder.getState();
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public NominalPublicationDate getNominalPublicationDate() {
        return nominalPublicationDate;
    }

    public List<Attachment> getAttachments() {
        return new ArrayList<>(attachments);
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
