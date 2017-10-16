package uk.nhs.digital.ps.test.acceptance.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

@SuppressWarnings("WeakerAccess") // builder's methods are intentionally public
public class PublicationBuilder {

    private String publicationName;
    private String publicationTitle;
    private String publicationSummary;
    private GeographicCoverage geographicCoverage;
    private InformationType informationType;
    private Granularity granularity;
    private Instant nominalDate;
    private List<AttachmentBuilder> attachmentBuilders = new ArrayList<>();
    private Taxonomy taxonomy;

    private PublicationStatus status;

    public static PublicationBuilder newPublication() {
        return new PublicationBuilder();
    }

    //<editor-fold desc="BUILDER METHODS">
    public PublicationBuilder withPublicationName(final String publicationName) {
        return cloneAndAmend(builder -> builder.publicationName = publicationName);
    }

    public PublicationBuilder withPublicationTitle(final String publicationTitle) {
        return cloneAndAmend(builder -> builder.publicationTitle = publicationTitle);
    }

    public PublicationBuilder withPublicationSummary(final String publicationSummary) {
        return cloneAndAmend(builder -> builder.publicationSummary = publicationSummary);
    }

    public PublicationBuilder withGeographicCoverage(final GeographicCoverage geographicCoverage) {
        return cloneAndAmend(builder -> builder.geographicCoverage = geographicCoverage);
    }

    public PublicationBuilder withInformationType(final InformationType informationType) {
        return cloneAndAmend(builder -> builder.informationType = informationType);
    }

    public PublicationBuilder withGranularity(final Granularity granularity) {
        return cloneAndAmend(builder -> builder.granularity = granularity);
    }

    public PublicationBuilder withNominalDate(final Instant nominalDate) {
        return cloneAndAmend(builder -> builder.nominalDate = nominalDate);
    }

    public PublicationBuilder withAttachments(final List<AttachmentBuilder> attachmentBuilders) {
        return cloneAndAmend(builder -> builder.attachmentBuilders = attachmentBuilders);
    }
    public PublicationBuilder withAttachments(final AttachmentBuilder... attachmentBuilders) {
        return cloneAndAmend(builder -> builder.attachmentBuilders = asList(attachmentBuilders));
    }

    public PublicationBuilder withStatus(final PublicationStatus status) {
        return cloneAndAmend(builder -> builder.status = status);
    }

    public PublicationBuilder withTaxonomy(final Taxonomy taxonomy) {
        return cloneAndAmend(builder -> builder.taxonomy = taxonomy);
    }
    //</editor-fold>

    public Publication build() {
        return new Publication(this);
    }
    //<editor-fold desc="GETTERS" defaultstate="collapsed">

    String getPublicationName() {
        return publicationName;
    }

    String getPublicationTitle() {
        return publicationTitle;
    }

    String getPublicationSummary() {
        return publicationSummary;
    }

    GeographicCoverage getGeographicCoverage() {
        return geographicCoverage;
    }

    InformationType getInformationType() {
        return informationType;
    }

    Granularity getGranularity() {
        return granularity;
    }

    Taxonomy getTaxonomy() {
        return taxonomy;
    }

    Instant getNominalDate() {
        return nominalDate;
    }

    List<Attachment> getAttachments() {
        return getAttachmentBuilders().stream().map(AttachmentBuilder::build).collect(toList());
    }

    List<AttachmentBuilder> getAttachmentBuilders() {
        return new ArrayList<>(attachmentBuilders);
    }

    PublicationStatus getStatus() {
        return status;
    }
    //</editor-fold>

    private PublicationBuilder(final PublicationBuilder original) {
        publicationName = original.getPublicationName();
        publicationTitle = original.getPublicationTitle();
        publicationSummary = original.getPublicationSummary();
        geographicCoverage = original.getGeographicCoverage();
        informationType = original.getInformationType();
        granularity = original.getGranularity();
        nominalDate = original.getNominalDate();
        taxonomy = original.getTaxonomy();
        attachmentBuilders = original.getAttachmentBuilders();

        status = original.getStatus();
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
