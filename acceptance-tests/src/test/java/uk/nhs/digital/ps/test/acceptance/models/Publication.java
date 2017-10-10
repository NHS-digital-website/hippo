package uk.nhs.digital.ps.test.acceptance.models;

import uk.nhs.digital.ps.test.acceptance.util.RandomHelper;

public class Publication {

    private String publicationName;
    private String publicationTitle;
    private String publicationSummary;
    private Attachment attachment;
    private String geographicCoverage;
    private String informationType;
    private String granularity;

    private Publication(final String publicationName,
                        final String publicationTitle,
                        final String publicationSummary,
                        final Attachment attachment,
                        final String geographicCoverage,
                        final String informationType,
                        final String granularity) {
        this.publicationName = publicationName;
        this.publicationTitle = publicationTitle;
        this.publicationSummary = publicationSummary;
        this.attachment = attachment;
        this.geographicCoverage = geographicCoverage;
        this.informationType = informationType;
        this.granularity = granularity;
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

    public Attachment getAttachment() {
        return attachment;
    }

    public String getGeographicCoverage() {
        return geographicCoverage;
    }

    public String getPublicationUrl() {
        return "/publications/" + publicationName;
    }

    public String getGranularity() {
        return granularity;
    }

    public String getInformationType() {
        return informationType;
    }

    /**
     * @return New instance, fully populated with random values.
     */
    public static Publication createNew() {

        return new Publication(
            RandomHelper.newRandomString(),
            RandomHelper.newRandomString(),
            RandomHelper.newRandomString(),
            Attachment.createNew(),
            "UK",
            "Experimental statistics",
            "Ambulance Trusts"
        );
    }
}
