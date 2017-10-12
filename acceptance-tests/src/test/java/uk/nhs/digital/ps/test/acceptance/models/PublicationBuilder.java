package uk.nhs.digital.ps.test.acceptance.models;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class PublicationBuilder {

    private String publicationName;
    private String publicationTitle;
    private String publicationSummary;
    private String geographicCoverage;
    private String informationType;
    private String granularity;
    private List<Attachment> attachments = new ArrayList<>();
    private Taxonomy taxonomy;

    public static PublicationBuilder create() {
        return new PublicationBuilder();
    }

    //<editor-fold desc="builder methods">
    public PublicationBuilder withPublicationName(final String publicationName) {
        return cloneAndAmend(builder -> builder.publicationName = publicationName);
    }

    public PublicationBuilder withPublicationTitle(final String publicationTitle) {
        return cloneAndAmend(builder -> builder.publicationTitle = publicationTitle);
    }

    public PublicationBuilder withPublicationSummary(final String publicationSummary) {
        return cloneAndAmend(builder -> builder.publicationSummary = publicationSummary);
    }

    public PublicationBuilder withGeographicCoverage(final String geographicCoverage) {
        return cloneAndAmend(builder -> builder.geographicCoverage = geographicCoverage);
    }

    public PublicationBuilder withInformationType(final String informationType) {
        return cloneAndAmend(builder -> builder.informationType = informationType);
    }

    public PublicationBuilder withGranularity(final String granularity) {
        return cloneAndAmend(builder -> builder.granularity = granularity);
    }

    public PublicationBuilder withTaxonomy(final Taxonomy taxonomy) {
        return cloneAndAmend(builder -> builder.taxonomy = taxonomy);
    }

    public PublicationBuilder withAttachments(final List<Attachment> attachments) {
        return cloneAndAmend(builder -> builder.attachments = attachments);
    }
    //</editor-fold>

    public Publication build() {
        return new Publication(this);
    }

    //<editor-fold desc="getters" defaultstate="collapsed">
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

    Taxonomy getTaxonomy() {
        return taxonomy;
    }

    List<Attachment> getAttachments() {
        return attachments == null ? new ArrayList<>() : new ArrayList<>(attachments);
    }
    //</editor-fold>

    private PublicationBuilder(final PublicationBuilder original) {
        publicationName = original.getPublicationName();
        publicationTitle = original.getPublicationTitle();
        publicationSummary = original.getPublicationSummary();
        geographicCoverage = original.getGeographicCoverage();
        informationType = original.getInformationType();
        granularity = original.getGranularity();
        attachments = getAttachments();
        taxonomy = original.getTaxonomy();
    }

    private PublicationBuilder() {
        // no-op; made private to promote the use of factory methods
    }

    private PublicationBuilder cloneAndAmend(final PropertySetter propertySetter) {
        final PublicationBuilder clone = new PublicationBuilder(this);
        propertySetter.setProperties(clone);
        return clone;
    }

    @FunctionalInterface
    interface PropertySetter {
        void setProperties(PublicationBuilder builder);
    }
}
