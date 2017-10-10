package uk.nhs.digital.ps.test.acceptance.models;

import java.util.ArrayList;
import java.util.List;

import static uk.nhs.digital.ps.test.acceptance.util.RandomHelper.newRandomString;

// Builder's methods are intentionally not private to promote their use and visibility in IDE outside of this class.
@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class PublicationBuilder {

    private String publicationName;
    private String publicationTitle;
    private String publicationSummary;
    private String geographicCoverage;
    private String informationType;
    private String granularity;
    private List<Attachment> attachments = new ArrayList<>();

    private PublicationBuilder() {
        // no-op made private to promote the use of factory methods
    }

    String getPublicationName() {
        return publicationName;
    }

    String getPublicationTitle() {
        return publicationTitle;
    }

    String getPublicationSummary() {
        return publicationSummary;
    }

    String getGeographicCoverage() {
        return geographicCoverage;
    }

    String getInformationType() {
        return informationType;
    }

    String getGranularity() {
        return granularity;
    }

    List<Attachment> getAttachments() {
        return attachments;
    }

    public PublicationBuilder withPublicationName(final String publicationName) {
        this.publicationName = publicationName;
        return this;
    }

    public PublicationBuilder withPublicationTitle(final String publicationTitle) {
        this.publicationTitle = publicationTitle;
        return this;
    }

    public PublicationBuilder withPublicationSummary(final String publicationSummary) {
        this.publicationSummary = publicationSummary;
        return this;
    }

    public PublicationBuilder withGeographicCoverage(final String geographicCoverage) {
        this.geographicCoverage = geographicCoverage;
        return this;
    }

    public PublicationBuilder withInformationType(final String informationType) {
        this.informationType = informationType;
        return this;
    }

    public PublicationBuilder withGranularity(final String granularity) {
        this.granularity = granularity;
        return this;
    }

    public PublicationBuilder withAttachments(final List<Attachment> attachments) {
        this.attachments = attachments;
        return this;
    }

    public Publication build() {
        return new Publication(this);
    }

    /**
     * @return New instance, fully populated with random or default values.
     */
    public static PublicationBuilder createValidPublication() {
        return new PublicationBuilder()
            .withPublicationName(newRandomString())
            .withPublicationTitle(newRandomString())
            .withPublicationSummary(newRandomString())
            .withGeographicCoverage("UK")
            .withInformationType("Experimental statistics")
            .withGranularity("Ambulance Trusts")
            .withAttachments(AttachmentBuilder.createValidAttachments());
    }
}
