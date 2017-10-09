package uk.nhs.digital.ps.test.acceptance.models;

import uk.nhs.digital.ps.test.acceptance.util.RandomHelper;

public class Publication {

    private String publicationName;
    private String publicationTitle;
    private String publicationSummary;
    private Attachment attachment;
    private String geographicCoverage;

    private Publication(final String publicationName,
                        final String publicationTitle,
                        final String publicationSummary,
                        final Attachment attachment,
                        final String geographicCoverage) {
        this.publicationName = publicationName;
        this.publicationTitle = publicationTitle;
        this.publicationSummary = publicationSummary;
        this.attachment = attachment;
        this.geographicCoverage = geographicCoverage;
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

    public String getPublicationUrl() {
        return "/publications/" + publicationName;
    }

    public Attachment getAttachment() {
        return attachment;
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
            "UK"
        );
    }
}
